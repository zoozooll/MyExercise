// Copyright 2007-2010 Baptiste Lepilleur
// Distributed under MIT license, or public domain if desired and
// recognized in your jurisdiction.
// See file LICENSE for detail or copy at http://jsoncpp.sourceforge.net/LICENSE

// included by json_value.cpp

namespace CFMJson {

// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// class FMValueInternalArray
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////

FMValueArrayAllocator::~FMValueArrayAllocator()
{
}

// //////////////////////////////////////////////////////////////////
// class DefaultFMValueArrayAllocator
// //////////////////////////////////////////////////////////////////
#ifdef FMJSON_USE_SIMPLE_INTERNAL_ALLOCATOR
class DefaultFMValueArrayAllocator : public FMValueArrayAllocator
{
public: // overridden from FMValueArrayAllocator
   virtual ~DefaultFMValueArrayAllocator()
   {
   }

   virtual FMValueInternalArray *newArray()
   {
      return new FMValueInternalArray();
   }

   virtual FMValueInternalArray *newArrayCopy( const FMValueInternalArray &other )
   {
      return new FMValueInternalArray( other );
   }

   virtual void destructArray( FMValueInternalArray *array )
   {
      delete array;
   }

   virtual void reallocateArrayPageIndex( FMValue **&indexes, 
                                          FMValueInternalArray::PageIndex &indexCount,
                                          FMValueInternalArray::PageIndex minNewIndexCount )
   {
      FMValueInternalArray::PageIndex newIndexCount = (indexCount*3)/2 + 1;
      if ( minNewIndexCount > newIndexCount )
         newIndexCount = minNewIndexCount;
      void *newIndexes = realloc( indexes, sizeof(FMValue*) * newIndexCount );
      if ( !newIndexes )
         throw std::bad_alloc();
      indexCount = newIndexCount;
      indexes = static_cast<FMValue **>( newIndexes );
   }
   virtual void releaseArrayPageIndex( FMValue **indexes, 
                                       FMValueInternalArray::PageIndex indexCount )
   {
      if ( indexes )
         free( indexes );
   }

   virtual FMValue *allocateArrayPage()
   {
      return static_cast<FMValue *>( malloc( sizeof(FMValue) * FMValueInternalArray::itemsPerPage ) );
   }

   virtual void releaseArrayPage( FMValue *FMvalue )
   {
      if ( FMvalue )
         free( FMvalue );
   }
};

#else // #ifdef FMJSON_USE_SIMPLE_INTERNAL_ALLOCATOR
/// @todo make this thread-safe (lock when accessign batch allocator)
class DefaultFMValueArrayAllocator : public FMValueArrayAllocator
{
public: // overridden from FMValueArrayAllocator
   virtual ~DefaultFMValueArrayAllocator()
   {
   }

   virtual FMValueInternalArray *newArray()
   {
      FMValueInternalArray *array = arraysAllocator_.allocate();
      new (array) FMValueInternalArray(); // placement new
      return array;
   }

   virtual FMValueInternalArray *newArrayCopy( const FMValueInternalArray &other )
   {
      FMValueInternalArray *array = arraysAllocator_.allocate();
      new (array) FMValueInternalArray( other ); // placement new
      return array;
   }

   virtual void destructArray( FMValueInternalArray *array )
   {
      if ( array )
      {
         array->~FMValueInternalArray();
         arraysAllocator_.release( array );
      }
   }

   virtual void reallocateArrayPageIndex( FMValue **&indexes, 
                                          FMValueInternalArray::PageIndex &indexCount,
                                          FMValueInternalArray::PageIndex minNewIndexCount )
   {
      FMValueInternalArray::PageIndex newIndexCount = (indexCount*3)/2 + 1;
      if ( minNewIndexCount > newIndexCount )
         newIndexCount = minNewIndexCount;
      void *newIndexes = realloc( indexes, sizeof(FMValue*) * newIndexCount );
      if ( !newIndexes )
         throw std::bad_alloc();
      indexCount = newIndexCount;
      indexes = static_cast<FMValue **>( newIndexes );
   }
   virtual void releaseArrayPageIndex( FMValue **indexes, 
                                       FMValueInternalArray::PageIndex indexCount )
   {
      if ( indexes )
         free( indexes );
   }

   virtual FMValue *allocateArrayPage()
   {
      return static_cast<FMValue *>( pagesAllocator_.allocate() );
   }

   virtual void releaseArrayPage( FMValue *FMvalue )
   {
      if ( FMvalue )
         pagesAllocator_.release( FMvalue );
   }
private:
   BatchAllocator<FMValueInternalArray,1> arraysAllocator_;
   BatchAllocator<FMValue,FMValueInternalArray::itemsPerPage> pagesAllocator_;
};
#endif // #ifdef FMJSON_USE_SIMPLE_INTERNAL_ALLOCATOR

static FMValueArrayAllocator *&arrayAllocator()
{
   static DefaultFMValueArrayAllocator defaultAllocator;
   static FMValueArrayAllocator *arrayAllocator = &defaultAllocator;
   return arrayAllocator;
}

static struct DummyArrayAllocatorInitializer {
   DummyArrayAllocatorInitializer() 
   {
      arrayAllocator();      // ensure arrayAllocator() statics are initialized before main().
   }
} dummyArrayAllocatorInitializer;

// //////////////////////////////////////////////////////////////////
// class FMValueInternalArray
// //////////////////////////////////////////////////////////////////
bool 
FMValueInternalArray::equals( const IteratorState &x, 
                            const IteratorState &other )
{
   return x.array_ == other.array_  
          &&  x.currentItemIndex_ == other.currentItemIndex_  
          &&  x.currentPageIndex_ == other.currentPageIndex_;
}


void 
FMValueInternalArray::increment( IteratorState &it )
{
   FMJSON_ASSERT_MESSAGE( it.array_  &&
      (it.currentPageIndex_ - it.array_->pages_)*itemsPerPage + it.currentItemIndex_
      != it.array_->size_,
      "FMValueInternalArray::increment(): moving iterator beyond end" );
   ++(it.currentItemIndex_);
   if ( it.currentItemIndex_ == itemsPerPage )
   {
      it.currentItemIndex_ = 0;
      ++(it.currentPageIndex_);
   }
}


void 
FMValueInternalArray::decrement( IteratorState &it )
{
   FMJSON_ASSERT_MESSAGE( it.array_  &&  it.currentPageIndex_ == it.array_->pages_ 
                        &&  it.currentItemIndex_ == 0,
      "FMValueInternalArray::decrement(): moving iterator beyond end" );
   if ( it.currentItemIndex_ == 0 )
   {
      it.currentItemIndex_ = itemsPerPage-1;
      --(it.currentPageIndex_);
   }
   else
   {
      --(it.currentItemIndex_);
   }
}


FMValue &
FMValueInternalArray::unsafeDereference( const IteratorState &it )
{
   return (*(it.currentPageIndex_))[it.currentItemIndex_];
}


FMValue &
FMValueInternalArray::dereference( const IteratorState &it )
{
   FMJSON_ASSERT_MESSAGE( it.array_  &&
      (it.currentPageIndex_ - it.array_->pages_)*itemsPerPage + it.currentItemIndex_
      < it.array_->size_,
      "FMValueInternalArray::dereference(): dereferencing invalid iterator" );
   return unsafeDereference( it );
}

void 
FMValueInternalArray::makeBeginIterator( IteratorState &it ) const
{
   it.array_ = const_cast<FMValueInternalArray *>( this );
   it.currentItemIndex_ = 0;
   it.currentPageIndex_ = pages_;
}


void 
FMValueInternalArray::makeIterator( IteratorState &it, ArrayIndex index ) const
{
   it.array_ = const_cast<FMValueInternalArray *>( this );
   it.currentItemIndex_ = index % itemsPerPage;
   it.currentPageIndex_ = pages_ + index / itemsPerPage;
}


void 
FMValueInternalArray::makeEndIterator( IteratorState &it ) const
{
   makeIterator( it, size_ );
}


FMValueInternalArray::FMValueInternalArray()
   : pages_( 0 )
   , size_( 0 )
   , pageCount_( 0 )
{
}


FMValueInternalArray::FMValueInternalArray( const FMValueInternalArray &other )
   : pages_( 0 )
   , pageCount_( 0 )
   , size_( other.size_ )
{
   PageIndex minNewPages = other.size_ / itemsPerPage;
   arrayAllocator()->reallocateArrayPageIndex( pages_, pageCount_, minNewPages );
   FMJSON_ASSERT_MESSAGE( pageCount_ >= minNewPages, 
                        "FMValueInternalArray::reserve(): bad reallocation" );
   IteratorState itOther;
   other.makeBeginIterator( itOther );
   FMValue *FMvalue;
   for ( ArrayIndex index = 0; index < size_; ++index, increment(itOther) )
   {
      if ( index % itemsPerPage == 0 )
      {
         PageIndex pageIndex = index / itemsPerPage;
         FMvalue = arrayAllocator()->allocateArrayPage();
         pages_[pageIndex] = FMvalue;
      }
      new (FMvalue) FMValue( dereference( itOther ) );
   }
}


FMValueInternalArray &
FMValueInternalArray::operator =( const FMValueInternalArray &other )
{
   FMValueInternalArray temp( other );
   swap( temp );
   return *this;
}


FMValueInternalArray::~FMValueInternalArray()
{
   // destroy all constructed items
   IteratorState it;
   IteratorState itEnd;
   makeBeginIterator( it);
   makeEndIterator( itEnd );
   for ( ; !equals(it,itEnd); increment(it) )
   {
      FMValue *FMvalue = &dereference(it);
      FMvalue->~FMValue();
   }
   // release all pages
   PageIndex lastPageIndex = size_ / itemsPerPage;
   for ( PageIndex pageIndex = 0; pageIndex < lastPageIndex; ++pageIndex )
      arrayAllocator()->releaseArrayPage( pages_[pageIndex] );
   // release pages index
   arrayAllocator()->releaseArrayPageIndex( pages_, pageCount_ );
}


void 
FMValueInternalArray::swap( FMValueInternalArray &other )
{
   FMValue **tempPages = pages_;
   pages_ = other.pages_;
   other.pages_ = tempPages;
   ArrayIndex tempSize = size_;
   size_ = other.size_;
   other.size_ = tempSize;
   PageIndex tempPageCount = pageCount_;
   pageCount_ = other.pageCount_;
   other.pageCount_ = tempPageCount;
}

void 
FMValueInternalArray::clear()
{
   FMValueInternalArray dummy;
   swap( dummy );
}


void 
FMValueInternalArray::resize( ArrayIndex newSize )
{
   if ( newSize == 0 )
      clear();
   else if ( newSize < size_ )
   {
      IteratorState it;
      IteratorState itEnd;
      makeIterator( it, newSize );
      makeIterator( itEnd, size_ );
      for ( ; !equals(it,itEnd); increment(it) )
      {
         FMValue *FMvalue = &dereference(it);
         FMvalue->~FMValue();
      }
      PageIndex pageIndex = (newSize + itemsPerPage - 1) / itemsPerPage;
      PageIndex lastPageIndex = size_ / itemsPerPage;
      for ( ; pageIndex < lastPageIndex; ++pageIndex )
         arrayAllocator()->releaseArrayPage( pages_[pageIndex] );
      size_ = newSize;
   }
   else if ( newSize > size_ )
      resolveReference( newSize );
}


void 
FMValueInternalArray::makeIndexValid( ArrayIndex index )
{
   // Need to enlarge page index ?
   if ( index >= pageCount_ * itemsPerPage )
   {
      PageIndex minNewPages = (index + 1) / itemsPerPage;
      arrayAllocator()->reallocateArrayPageIndex( pages_, pageCount_, minNewPages );
      FMJSON_ASSERT_MESSAGE( pageCount_ >= minNewPages, "FMValueInternalArray::reserve(): bad reallocation" );
   }

   // Need to allocate new pages ?
   ArrayIndex nextPageIndex = 
      (size_ % itemsPerPage) != 0 ? size_ - (size_%itemsPerPage) + itemsPerPage
                                  : size_;
   if ( nextPageIndex <= index )
   {
      PageIndex pageIndex = nextPageIndex / itemsPerPage;
      PageIndex pageToAllocate = (index - nextPageIndex) / itemsPerPage + 1;
      for ( ; pageToAllocate-- > 0; ++pageIndex )
         pages_[pageIndex] = arrayAllocator()->allocateArrayPage();
   }

   // Initialize all new entries
   IteratorState it;
   IteratorState itEnd;
   makeIterator( it, size_ );
   size_ = index + 1;
   makeIterator( itEnd, size_ );
   for ( ; !equals(it,itEnd); increment(it) )
   {
      FMValue *FMvalue = &dereference(it);
      new (FMvalue) FMValue(); // Construct a default FMvalue using placement new
   }
}

FMValue &
FMValueInternalArray::resolveReference( ArrayIndex index )
{
   if ( index >= size_ )
      makeIndexValid( index );
   return pages_[index/itemsPerPage][index%itemsPerPage];
}

FMValue *
FMValueInternalArray::find( ArrayIndex index ) const
{
   if ( index >= size_ )
      return 0;
   return &(pages_[index/itemsPerPage][index%itemsPerPage]);
}

FMValueInternalArray::ArrayIndex 
FMValueInternalArray::size() const
{
   return size_;
}

int 
FMValueInternalArray::distance( const IteratorState &x, const IteratorState &y )
{
   return indexOf(y) - indexOf(x);
}


FMValueInternalArray::ArrayIndex 
FMValueInternalArray::indexOf( const IteratorState &iterator )
{
   if ( !iterator.array_ )
      return ArrayIndex(-1);
   return ArrayIndex(
      (iterator.currentPageIndex_ - iterator.array_->pages_) * itemsPerPage 
      + iterator.currentItemIndex_ );
}


int 
FMValueInternalArray::compare( const FMValueInternalArray &other ) const
{
   int sizeDiff( size_ - other.size_ );
   if ( sizeDiff != 0 )
      return sizeDiff;
   
   for ( ArrayIndex index =0; index < size_; ++index )
   {
      int diff = pages_[index/itemsPerPage][index%itemsPerPage].compare( 
         other.pages_[index/itemsPerPage][index%itemsPerPage] );
      if ( diff != 0 )
         return diff;
   }
   return 0;
}

} // namespace CFMJson
