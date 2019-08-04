// Copyright 2007-2010 Baptiste Lepilleur
// Distributed under MIT license, or public domain if desired and
// recognized in your jurisdiction.
// See file LICENSE for detail or copy at http://jsoncpp.sourceforge.net/LICENSE

// included by json_value.cpp

namespace CFMJson {

// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// class FMValueIteratorBase
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////

FMValueIteratorBase::FMValueIteratorBase()
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   : current_()
   , isNull_( true )
{
}
#else
   : isArray_( true )
   , isNull_( true )
{
   iterator_.array_ = FMValueInternalArray::IteratorState();
}
#endif


#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
FMValueIteratorBase::FMValueIteratorBase( const FMValue::ObjectFMValues::iterator &current )
   : current_( current )
   , isNull_( false )
{
}
#else
FMValueIteratorBase::FMValueIteratorBase( const FMValueInternalArray::IteratorState &state )
   : isArray_( true )
{
   iterator_.array_ = state;
}


FMValueIteratorBase::FMValueIteratorBase( const FMValueInternalMap::IteratorState &state )
   : isArray_( false )
{
   iterator_.map_ = state;
}
#endif

FMValue &
FMValueIteratorBase::deref() const
{
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   return current_->second;
#else
   if ( isArray_ )
      return FMValueInternalArray::dereference( iterator_.array_ );
   return FMValueInternalMap::FMvalue( iterator_.map_ );
#endif
}


void 
FMValueIteratorBase::increment()
{
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   ++current_;
#else
   if ( isArray_ )
      FMValueInternalArray::increment( iterator_.array_ );
   FMValueInternalMap::increment( iterator_.map_ );
#endif
}


void 
FMValueIteratorBase::decrement()
{
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   --current_;
#else
   if ( isArray_ )
      FMValueInternalArray::decrement( iterator_.array_ );
   FMValueInternalMap::decrement( iterator_.map_ );
#endif
}


FMValueIteratorBase::difference_type 
FMValueIteratorBase::computeDistance( const SelfType &other ) const
{
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
# ifdef FMJSON_USE_FMCPPTL_SMALLMAP
   return current_ - other.current_;
# else
   // Iterator for null FMvalue are initialized using the default
   // constructor, which initialize current_ to the default
   // std::map::iterator. As begin() and end() are two instance 
   // of the default std::map::iterator, they can not be compared.
   // To allow this, we handle this comparison specifically.
   if ( isNull_  &&  other.isNull_ )
   {
      return 0;
   }


   // Usage of std::distance is not portable (does not compile with Sun Studio 12 RogueWave STL,
   // which is the one used by default).
   // Using a portable hand-made version for non random iterator instead:
   //   return difference_type( std::distance( current_, other.current_ ) );
   difference_type myDistance = 0;
   for ( FMValue::ObjectFMValues::iterator it = current_; it != other.current_; ++it )
   {
      ++myDistance;
   }
   return myDistance;
# endif
#else
   if ( isArray_ )
      return FMValueInternalArray::distance( iterator_.array_, other.iterator_.array_ );
   return FMValueInternalMap::distance( iterator_.map_, other.iterator_.map_ );
#endif
}


bool 
FMValueIteratorBase::isEqual( const SelfType &other ) const
{
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   if ( isNull_ )
   {
      return other.isNull_;
   }
   return current_ == other.current_;
#else
   if ( isArray_ )
      return FMValueInternalArray::equals( iterator_.array_, other.iterator_.array_ );
   return FMValueInternalMap::equals( iterator_.map_, other.iterator_.map_ );
#endif
}


void 
FMValueIteratorBase::copy( const SelfType &other )
{
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   current_ = other.current_;
#else
   if ( isArray_ )
      iterator_.array_ = other.iterator_.array_;
   iterator_.map_ = other.iterator_.map_;
#endif
}


FMValue 
FMValueIteratorBase::key() const
{
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   const FMValue::CZString czstring = (*current_).first;
   if ( czstring.c_str() )
   {
      if ( czstring.isStaticString() )
         return FMValue( StaticString( czstring.c_str() ) );
      return FMValue( czstring.c_str() );
   }
   return FMValue( czstring.index() );
#else
   if ( isArray_ )
      return FMValue( FMValueInternalArray::indexOf( iterator_.array_ ) );
   bool isStatic;
   const char *memberName = FMValueInternalMap::key( iterator_.map_, isStatic );
   if ( isStatic )
      return FMValue( StaticString( memberName ) );
   return FMValue( memberName );
#endif
}


UInt 
FMValueIteratorBase::index() const
{
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   const FMValue::CZString czstring = (*current_).first;
   if ( !czstring.c_str() )
      return czstring.index();
   return FMValue::UInt( -1 );
#else
   if ( isArray_ )
      return FMValue::UInt( FMValueInternalArray::indexOf( iterator_.array_ ) );
   return FMValue::UInt( -1 );
#endif
}


const char *
FMValueIteratorBase::memberName() const
{
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   const char *name = (*current_).first.c_str();
   return name ? name : "";
#else
   if ( !isArray_ )
      return FMValueInternalMap::key( iterator_.map_ );
   return "";
#endif
}


// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// class FMValueConstIterator
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////

FMValueConstIterator::FMValueConstIterator()
{
}


#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
FMValueConstIterator::FMValueConstIterator( const FMValue::ObjectFMValues::iterator &current )
   : FMValueIteratorBase( current )
{
}
#else
FMValueConstIterator::FMValueConstIterator( const FMValueInternalArray::IteratorState &state )
   : FMValueIteratorBase( state )
{
}

FMValueConstIterator::FMValueConstIterator( const FMValueInternalMap::IteratorState &state )
   : FMValueIteratorBase( state )
{
}
#endif

FMValueConstIterator &
FMValueConstIterator::operator =( const FMValueIteratorBase &other )
{
   copy( other );
   return *this;
}


// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// class FMValueIterator
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////

FMValueIterator::FMValueIterator()
{
}


#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
FMValueIterator::FMValueIterator( const FMValue::ObjectFMValues::iterator &current )
   : FMValueIteratorBase( current )
{
}
#else
FMValueIterator::FMValueIterator( const FMValueInternalArray::IteratorState &state )
   : FMValueIteratorBase( state )
{
}

FMValueIterator::FMValueIterator( const FMValueInternalMap::IteratorState &state )
   : FMValueIteratorBase( state )
{
}
#endif

FMValueIterator::FMValueIterator( const FMValueConstIterator &other )
   : FMValueIteratorBase( other )
{
}

FMValueIterator::FMValueIterator( const FMValueIterator &other )
   : FMValueIteratorBase( other )
{
}

FMValueIterator &
FMValueIterator::operator =( const SelfType &other )
{
   copy( other );
   return *this;
}

} // namespace CFMJson
