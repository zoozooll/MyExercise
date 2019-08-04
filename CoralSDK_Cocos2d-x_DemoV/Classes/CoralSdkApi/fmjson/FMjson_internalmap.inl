// Copyright 2007-2010 Baptiste Lepilleur
// Distributed under MIT license, or public domain if desired and
// recognized in your jurisdiction.
// See file LICENSE for detail or copy at http://jsoncpp.sourceforge.net/LICENSE

// included by json_value.cpp

namespace CFMJson {

// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// class FMValueInternalMap
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////

/** \internal MUST be safely initialized using memset( this, 0, sizeof(FMValueInternalLink) );
   * This optimization is used by the fast allocator.
   */
FMValueInternalLink::FMValueInternalLink()
   : previous_( 0 )
   , next_( 0 )
{
}

FMValueInternalLink::~FMValueInternalLink()
{ 
   for ( int index =0; index < itemPerLink; ++index )
   {
      if ( !items_[index].isItemAvailable() )
      {
         if ( !items_[index].isMemberNameStatic() )
            free( keys_[index] );
      }
      else
         break;
   }
}



FMValueMapAllocator::~FMValueMapAllocator()
{
}

#ifdef FMJSON_USE_SIMPLE_INTERNAL_ALLOCATOR
class DefaultFMValueMapAllocator : public FMValueMapAllocator
{
public: // overridden from FMValueMapAllocator
   virtual FMValueInternalMap *newMap()
   {
      return new FMValueInternalMap();
   }

   virtual FMValueInternalMap *newMapCopy( const FMValueInternalMap &other )
   {
      return new FMValueInternalMap( other );
   }

   virtual void destructMap( FMValueInternalMap *map )
   {
      delete map;
   }

   virtual FMValueInternalLink *allocateMapBuckets( unsigned int size )
   {
      return new FMValueInternalLink[size];
   }

   virtual void releaseMapBuckets( FMValueInternalLink *links )
   {
      delete [] links;
   }

   virtual FMValueInternalLink *allocateMapLink()
   {
      return new FMValueInternalLink();
   }

   virtual void releaseMapLink( FMValueInternalLink *link )
   {
      delete link;
   }
};
#else
/// @todo make this thread-safe (lock when accessign batch allocator)
class DefaultFMValueMapAllocator : public FMValueMapAllocator
{
public: // overridden from FMValueMapAllocator
   virtual FMValueInternalMap *newMap()
   {
      FMValueInternalMap *map = mapsAllocator_.allocate();
      new (map) FMValueInternalMap(); // placement new
      return map;
   }

   virtual FMValueInternalMap *newMapCopy( const FMValueInternalMap &other )
   {
      FMValueInternalMap *map = mapsAllocator_.allocate();
      new (map) FMValueInternalMap( other ); // placement new
      return map;
   }

   virtual void destructMap( FMValueInternalMap *map )
   {
      if ( map )
      {
         map->~FMValueInternalMap();
         mapsAllocator_.release( map );
      }
   }

   virtual FMValueInternalLink *allocateMapBuckets( unsigned int size )
   {
      return new FMValueInternalLink[size];
   }

   virtual void releaseMapBuckets( FMValueInternalLink *links )
   {
      delete [] links;
   }

   virtual FMValueInternalLink *allocateMapLink()
   {
      FMValueInternalLink *link = linksAllocator_.allocate();
      memset( link, 0, sizeof(FMValueInternalLink) );
      return link;
   }

   virtual void releaseMapLink( FMValueInternalLink *link )
   {
      link->~FMValueInternalLink();
      linksAllocator_.release( link );
   }
private:
   BatchAllocator<FMValueInternalMap,1> mapsAllocator_;
   BatchAllocator<FMValueInternalLink,1> linksAllocator_;
};
#endif

static FMValueMapAllocator *&mapAllocator()
{
   static DefaultFMValueMapAllocator defaultAllocator;
   static FMValueMapAllocator *mapAllocator = &defaultAllocator;
   return mapAllocator;
}

static struct DummyMapAllocatorInitializer {
   DummyMapAllocatorInitializer() 
   {
      mapAllocator();      // ensure mapAllocator() statics are initialized before main().
   }
} dummyMapAllocatorInitializer;



// h(K) = FMvalue * K >> w ; with w = 32 & K prime w.r.t. 2^32.

/*
use linked list hash map. 
buckets array is a container.
linked list element contains 6 key/FMvalues. (memory = (16+4) * 6 + 4 = 124)
FMvalue have extra state: valid, available, deleted
*/


FMValueInternalMap::FMValueInternalMap()
   : buckets_( 0 )
   , tailLink_( 0 )
   , bucketsSize_( 0 )
   , itemCount_( 0 )
{
}


FMValueInternalMap::FMValueInternalMap( const FMValueInternalMap &other )
   : buckets_( 0 )
   , tailLink_( 0 )
   , bucketsSize_( 0 )
   , itemCount_( 0 )
{
   reserve( other.itemCount_ );
   IteratorState it;
   IteratorState itEnd;
   other.makeBeginIterator( it );
   other.makeEndIterator( itEnd );
   for ( ; !equals(it,itEnd); increment(it) )
   {
      bool isStatic;
      const char *memberName = key( it, isStatic );
      const FMValue &aFMValue = FMvalue( it );
      resolveReference(memberName, isStatic) = aFMValue;
   }
}


FMValueInternalMap &
FMValueInternalMap::operator =( const FMValueInternalMap &other )
{
   FMValueInternalMap dummy( other );
   swap( dummy );
   return *this;
}


FMValueInternalMap::~FMValueInternalMap()
{
   if ( buckets_ )
   {
      for ( BucketIndex bucketIndex =0; bucketIndex < bucketsSize_; ++bucketIndex )
      {
         FMValueInternalLink *link = buckets_[bucketIndex].next_;
         while ( link )
         {
            FMValueInternalLink *linkToRelease = link;
            link = link->next_;
            mapAllocator()->releaseMapLink( linkToRelease );
         }
      }
      mapAllocator()->releaseMapBuckets( buckets_ );
   }
}


void 
FMValueInternalMap::swap( FMValueInternalMap &other )
{
   FMValueInternalLink *tempBuckets = buckets_;
   buckets_ = other.buckets_;
   other.buckets_ = tempBuckets;
   FMValueInternalLink *tempTailLink = tailLink_;
   tailLink_ = other.tailLink_;
   other.tailLink_ = tempTailLink;
   BucketIndex tempBucketsSize = bucketsSize_;
   bucketsSize_ = other.bucketsSize_;
   other.bucketsSize_ = tempBucketsSize;
   BucketIndex tempItemCount = itemCount_;
   itemCount_ = other.itemCount_;
   other.itemCount_ = tempItemCount;
}


void 
FMValueInternalMap::clear()
{
   FMValueInternalMap dummy;
   swap( dummy );
}


FMValueInternalMap::BucketIndex 
FMValueInternalMap::size() const
{
   return itemCount_;
}

bool 
FMValueInternalMap::reserveDelta( BucketIndex growth )
{
   return reserve( itemCount_ + growth );
}

bool 
FMValueInternalMap::reserve( BucketIndex newItemCount )
{
   if ( !buckets_  &&  newItemCount > 0 )
   {
      buckets_ = mapAllocator()->allocateMapBuckets( 1 );
      bucketsSize_ = 1;
      tailLink_ = &buckets_[0];
   }
//   BucketIndex idealBucketCount = (newItemCount + FMValueInternalLink::itemPerLink) / FMValueInternalLink::itemPerLink;
   return true;
}


const FMValue *
FMValueInternalMap::find( const char *key ) const
{
   if ( !bucketsSize_ )
      return 0;
   HashKey hashedKey = hash( key );
   BucketIndex bucketIndex = hashedKey % bucketsSize_;
   for ( const FMValueInternalLink *current = &buckets_[bucketIndex]; 
         current != 0; 
         current = current->next_ )
   {
      for ( BucketIndex index=0; index < FMValueInternalLink::itemPerLink; ++index )
      {
         if ( current->items_[index].isItemAvailable() )
            return 0;
         if ( strcmp( key, current->keys_[index] ) == 0 )
            return &current->items_[index];
      }
   }
   return 0;
}


FMValue *
FMValueInternalMap::find( const char *key )
{
   const FMValueInternalMap *constThis = this;
   return const_cast<FMValue *>( constThis->find( key ) );
}


FMValue &
FMValueInternalMap::resolveReference( const char *key,
                                    bool isStatic )
{
   HashKey hashedKey = hash( key );
   if ( bucketsSize_ )
   {
      BucketIndex bucketIndex = hashedKey % bucketsSize_;
      FMValueInternalLink **previous = 0;
      BucketIndex index;
      for ( FMValueInternalLink *current = &buckets_[bucketIndex]; 
            current != 0; 
            previous = &current->next_, current = current->next_ )
      {
         for ( index=0; index < FMValueInternalLink::itemPerLink; ++index )
         {
            if ( current->items_[index].isItemAvailable() )
               return setNewItem( key, isStatic, current, index );
            if ( strcmp( key, current->keys_[index] ) == 0 )
               return current->items_[index];
         }
      }
   }

   reserveDelta( 1 );
   return unsafeAdd( key, isStatic, hashedKey );
}


void 
FMValueInternalMap::remove( const char *key )
{
   HashKey hashedKey = hash( key );
   if ( !bucketsSize_ )
      return;
   BucketIndex bucketIndex = hashedKey % bucketsSize_;
   for ( FMValueInternalLink *link = &buckets_[bucketIndex]; 
         link != 0; 
         link = link->next_ )
   {
      BucketIndex index;
      for ( index =0; index < FMValueInternalLink::itemPerLink; ++index )
      {
         if ( link->items_[index].isItemAvailable() )
            return;
         if ( strcmp( key, link->keys_[index] ) == 0 )
         {
            doActualRemove( link, index, bucketIndex );
            return;
         }
      }
   }
}

void 
FMValueInternalMap::doActualRemove( FMValueInternalLink *link, 
                                  BucketIndex index,
                                  BucketIndex bucketIndex )
{
   // find last item of the bucket and swap it with the 'removed' one.
   // set removed items flags to 'available'.
   // if last page only contains 'available' items, then desallocate it (it's empty)
   FMValueInternalLink *&lastLink = getLastLinkInBucket( index );
   BucketIndex lastItemIndex = 1; // a link can never be empty, so start at 1
   for ( ;   
         lastItemIndex < FMValueInternalLink::itemPerLink; 
         ++lastItemIndex ) // may be optimized with dicotomic search
   {
      if ( lastLink->items_[lastItemIndex].isItemAvailable() )
         break;
   }
   
   BucketIndex lastUsedIndex = lastItemIndex - 1;
   FMValue *FMvalueToDelete = &link->items_[index];
   FMValue *FMvalueToPreserve = &lastLink->items_[lastUsedIndex];
   if ( FMvalueToDelete != FMvalueToPreserve )
      FMvalueToDelete->swap( *FMvalueToPreserve );
   if ( lastUsedIndex == 0 )  // page is now empty
   {  // remove it from bucket linked list and delete it.
      FMValueInternalLink *linkPreviousToLast = lastLink->previous_;
      if ( linkPreviousToLast != 0 )   // can not deleted bucket link.
      {
         mapAllocator()->releaseMapLink( lastLink );
         linkPreviousToLast->next_ = 0;
         lastLink = linkPreviousToLast;
      }
   }
   else
   {
      FMValue dummy;
      FMvalueToPreserve->swap( dummy ); // restore deleted to default FMValue.
      FMvalueToPreserve->setItemUsed( false );
   }
   --itemCount_;
}


FMValueInternalLink *&
FMValueInternalMap::getLastLinkInBucket( BucketIndex bucketIndex )
{
   if ( bucketIndex == bucketsSize_ - 1 )
      return tailLink_;
   FMValueInternalLink *&previous = buckets_[bucketIndex+1].previous_;
   if ( !previous )
      previous = &buckets_[bucketIndex];
   return previous;
}


FMValue &
FMValueInternalMap::setNewItem( const char *key, 
                              bool isStatic,
                              FMValueInternalLink *link, 
                              BucketIndex index )
{
   char *duplicatedKey = makeMemberName( key );
   ++itemCount_;
   link->keys_[index] = duplicatedKey;
   link->items_[index].setItemUsed();
   link->items_[index].setMemberNameIsStatic( isStatic );
   return link->items_[index]; // items already default constructed.
}


FMValue &
FMValueInternalMap::unsafeAdd( const char *key, 
                             bool isStatic, 
                             HashKey hashedKey )
{
   FMJSON_ASSERT_MESSAGE( bucketsSize_ > 0, "FMValueInternalMap::unsafeAdd(): internal logic error." );
   BucketIndex bucketIndex = hashedKey % bucketsSize_;
   FMValueInternalLink *&previousLink = getLastLinkInBucket( bucketIndex );
   FMValueInternalLink *link = previousLink;
   BucketIndex index;
   for ( index =0; index < FMValueInternalLink::itemPerLink; ++index )
   {
      if ( link->items_[index].isItemAvailable() )
         break;
   }
   if ( index == FMValueInternalLink::itemPerLink ) // need to add a new page
   {
      FMValueInternalLink *newLink = mapAllocator()->allocateMapLink();
      index = 0;
      link->next_ = newLink;
      previousLink = newLink;
      link = newLink;
   }
   return setNewItem( key, isStatic, link, index );
}


FMValueInternalMap::HashKey 
FMValueInternalMap::hash( const char *key ) const
{
   HashKey hash = 0;
   while ( *key )
      hash += *key++ * 37;
   return hash;
}


int 
FMValueInternalMap::compare( const FMValueInternalMap &other ) const
{
   int sizeDiff( itemCount_ - other.itemCount_ );
   if ( sizeDiff != 0 )
      return sizeDiff;
   // Strict order guaranty is required. Compare all keys FIRST, then compare FMvalues.
   IteratorState it;
   IteratorState itEnd;
   makeBeginIterator( it );
   makeEndIterator( itEnd );
   for ( ; !equals(it,itEnd); increment(it) )
   {
      if ( !other.find( key( it ) ) )
         return 1;
   }

   // All keys are equals, let's compare FMvalues
   makeBeginIterator( it );
   for ( ; !equals(it,itEnd); increment(it) )
   {
      const FMValue *otherFMValue = other.find( key( it ) );
      int FMvalueDiff = FMvalue(it).compare( *otherFMValue );
      if ( FMvalueDiff != 0 )
         return FMvalueDiff;
   }
   return 0;
}


void 
FMValueInternalMap::makeBeginIterator( IteratorState &it ) const
{
   it.map_ = const_cast<FMValueInternalMap *>( this );
   it.bucketIndex_ = 0;
   it.itemIndex_ = 0;
   it.link_ = buckets_;
}


void 
FMValueInternalMap::makeEndIterator( IteratorState &it ) const
{
   it.map_ = const_cast<FMValueInternalMap *>( this );
   it.bucketIndex_ = bucketsSize_;
   it.itemIndex_ = 0;
   it.link_ = 0;
}


bool 
FMValueInternalMap::equals( const IteratorState &x, const IteratorState &other )
{
   return x.map_ == other.map_  
          &&  x.bucketIndex_ == other.bucketIndex_  
          &&  x.link_ == other.link_
          &&  x.itemIndex_ == other.itemIndex_;
}


void 
FMValueInternalMap::incrementBucket( IteratorState &iterator )
{
   ++iterator.bucketIndex_;
   FMJSON_ASSERT_MESSAGE( iterator.bucketIndex_ <= iterator.map_->bucketsSize_,
      "FMValueInternalMap::increment(): attempting to iterate beyond end." );
   if ( iterator.bucketIndex_ == iterator.map_->bucketsSize_ )
      iterator.link_ = 0;
   else
      iterator.link_ = &(iterator.map_->buckets_[iterator.bucketIndex_]);
   iterator.itemIndex_ = 0;
}


void 
FMValueInternalMap::increment( IteratorState &iterator )
{
   FMJSON_ASSERT_MESSAGE( iterator.map_, "Attempting to iterator using invalid iterator." );
   ++iterator.itemIndex_;
   if ( iterator.itemIndex_ == FMValueInternalLink::itemPerLink )
   {
      FMJSON_ASSERT_MESSAGE( iterator.link_ != 0,
         "FMValueInternalMap::increment(): attempting to iterate beyond end." );
      iterator.link_ = iterator.link_->next_;
      if ( iterator.link_ == 0 )
         incrementBucket( iterator );
   }
   else if ( iterator.link_->items_[iterator.itemIndex_].isItemAvailable() )
   {
      incrementBucket( iterator );
   }
}


void 
FMValueInternalMap::decrement( IteratorState &iterator )
{
   if ( iterator.itemIndex_ == 0 )
   {
      FMJSON_ASSERT_MESSAGE( iterator.map_, "Attempting to iterate using invalid iterator." );
      if ( iterator.link_ == &iterator.map_->buckets_[iterator.bucketIndex_] )
      {
         FMJSON_ASSERT_MESSAGE( iterator.bucketIndex_ > 0, "Attempting to iterate beyond beginning." );
         --(iterator.bucketIndex_);
      }
      iterator.link_ = iterator.link_->previous_;
      iterator.itemIndex_ = FMValueInternalLink::itemPerLink - 1;
   }
}


const char *
FMValueInternalMap::key( const IteratorState &iterator )
{
   FMJSON_ASSERT_MESSAGE( iterator.link_, "Attempting to iterate using invalid iterator." );
   return iterator.link_->keys_[iterator.itemIndex_];
}

const char *
FMValueInternalMap::key( const IteratorState &iterator, bool &isStatic )
{
   FMJSON_ASSERT_MESSAGE( iterator.link_, "Attempting to iterate using invalid iterator." );
   isStatic = iterator.link_->items_[iterator.itemIndex_].isMemberNameStatic();
   return iterator.link_->keys_[iterator.itemIndex_];
}


FMValue &
FMValueInternalMap::FMvalue( const IteratorState &iterator )
{
   FMJSON_ASSERT_MESSAGE( iterator.link_, "Attempting to iterate using invalid iterator." );
   return iterator.link_->items_[iterator.itemIndex_];
}


int 
FMValueInternalMap::distance( const IteratorState &x, const IteratorState &y )
{
   int offset = 0;
   IteratorState it = x;
   while ( !equals( it, y ) )
      increment( it );
   return offset;
}

} // namespace CFMJson
