// Copyright 2007-2010 Baptiste Lepilleur
// Distributed under MIT license, or public domain if desired and
// recognized in your jurisdiction.
// See file LICENSE for detail or copy at http://jsoncpp.sourceforge.net/LICENSE

#if !defined(FMJSON_IS_AMALGAMATION)
# include "FMvalue.h"
# include "FMwriter.h"
# ifndef FMJSON_USE_SIMPLE_INTERNAL_ALLOCATOR
#  include "FMjson_batchallocator.h"
# endif // #ifndef FMJSON_USE_SIMPLE_INTERNAL_ALLOCATOR
#endif // if !defined(FMJSON_IS_AMALGAMATION)
#include <iostream>
#include <utility>
#include <stdexcept>
#include <cstring>
#include <cassert>
#ifdef FMJSON_USE_CPPTL
# include <cpptl/conststring.h>
#endif
#include <cstddef>    // size_t

#include <string.h>

#define FMJSON_ASSERT_UNREACHABLE assert( false )
#define FMJSON_ASSERT( condition ) assert( condition );  // @todo <= change this into an exception throw
#define FMJSON_FAIL_MESSAGE( message ) throw std::runtime_error( message );
#define FMJSON_ASSERT_MESSAGE( condition, message ) if (!( condition )) FMJSON_FAIL_MESSAGE( message )

namespace CFMJson {

const FMValue FMValue::jsonNull;
const Int FMValue::minInt = Int( ~(UInt(-1)/2) );
const Int FMValue::maxInt = Int( UInt(-1)/2 );
const UInt FMValue::maxUInt = UInt(-1);
const Int64 FMValue::minInt64 = Int64( ~(UInt64(-1)/2) );
const Int64 FMValue::maxInt64 = Int64( UInt64(-1)/2 );
const UInt64 FMValue::maxUInt64 = UInt64(-1);
const LargestInt FMValue::minLargestInt = LargestInt( ~(LargestUInt(-1)/2) );
const LargestInt FMValue::maxLargestInt = LargestInt( LargestUInt(-1)/2 );
const LargestUInt FMValue::maxLargestUInt = LargestUInt(-1);


/// Unknown size marker
static const unsigned int unknown = (unsigned)-1;


/** Duplicates the specified string FMvalue.
 * @param FMvalue Pointer to the string to duplicate. Must be zero-terminated if
 *              length is "unknown".
 * @param length Length of the FMvalue. if equals to unknown, then it will be
 *               computed using strlen(FMvalue).
 * @return Pointer on the duplicate instance of string.
 */
static inline char *
duplicateStringFMValue( const char *FMvalue, 
                      unsigned int length = unknown )
{
   if ( length == unknown )
      length = (unsigned int)strlen(FMvalue);
   char *newString = static_cast<char *>( malloc( length + 1 ) );
   FMJSON_ASSERT_MESSAGE( newString != 0, "Failed to allocate string FMvalue buffer" );
   memcpy( newString, FMvalue, length );
   newString[length] = 0;
   return newString;
}


/** Free the string duplicated by duplicateStringFMValue().
 */
static inline void 
releaseStringFMValue( char *FMvalue )
{
   if ( FMvalue )
      free( FMvalue );
}

} // namespace CFMJson


// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// FMValueInternals...
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
#if !defined(FMJSON_IS_AMALGAMATION)
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
#  include "json_internalarray.inl"
#  include "json_internalmap.inl"
# endif // FMJSON_VALUE_USE_INTERNAL_MAP

# include "FMjson_valueiterator.inl"
#endif // if !defined(FMJSON_IS_AMALGAMATION)

namespace CFMJson {

// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// class FMValue::CommentInfo
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////


FMValue::CommentInfo::CommentInfo()
   : comment_( 0 )
{
}

FMValue::CommentInfo::~CommentInfo()
{
   if ( comment_ )
      releaseStringFMValue( comment_ );
}


void 
FMValue::CommentInfo::setComment( const char *text )
{
   if ( comment_ )
      releaseStringFMValue( comment_ );
   FMJSON_ASSERT( text != 0 );
   FMJSON_ASSERT_MESSAGE( text[0]=='\0' || text[0]=='/', "Comments must start with /");
   // It seems that /**/ style comments are acceptable as well.
   comment_ = duplicateStringFMValue( text );
}


// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// class FMValue::CZString
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
# ifndef FMJSON_VALUE_USE_INTERNAL_MAP

// Notes: index_ indicates if the string was allocated when
// a string is stored.

FMValue::CZString::CZString( ArrayIndex index )
   : cstr_( 0 )
   , index_( index )
{
}

FMValue::CZString::CZString( const char *cstr, DuplicationPolicy allocate )
   : cstr_( allocate == duplicate ? duplicateStringFMValue(cstr) 
                                  : cstr )
   , index_( allocate )
{
}

FMValue::CZString::CZString( const CZString &other )
: cstr_( other.index_ != noDuplication &&  other.cstr_ != 0
                ?  duplicateStringFMValue( other.cstr_ )
                : other.cstr_ )
   , index_( other.cstr_ ? (other.index_ == noDuplication ? noDuplication : duplicate)
                         : other.index_ )
{
}

FMValue::CZString::~CZString()
{
   if ( cstr_  &&  index_ == duplicate )
      releaseStringFMValue( const_cast<char *>( cstr_ ) );
}

void 
FMValue::CZString::swap( CZString &other )
{
   std::swap( cstr_, other.cstr_ );
   std::swap( index_, other.index_ );
}

FMValue::CZString &
FMValue::CZString::operator =( const CZString &other )
{
   CZString temp( other );
   swap( temp );
   return *this;
}

bool 
FMValue::CZString::operator<( const CZString &other ) const 
{
   if ( cstr_ )
      return strcmp( cstr_, other.cstr_ ) < 0;
   return index_ < other.index_;
}

bool 
FMValue::CZString::operator==( const CZString &other ) const 
{
   if ( cstr_ )
      return strcmp( cstr_, other.cstr_ ) == 0;
   return index_ == other.index_;
}


ArrayIndex 
FMValue::CZString::index() const
{
   return index_;
}


const char *
FMValue::CZString::c_str() const
{
   return cstr_;
}

bool 
FMValue::CZString::isStaticString() const
{
   return index_ == noDuplication;
}

#endif // ifndef FMJSON_VALUE_USE_INTERNAL_MAP


// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// class FMValue::FMValue
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////

/*! \internal Default constructor initialization must be equivalent to:
 * memset( this, 0, sizeof(FMValue) )
 * This optimization is used in FMValueInternalMap fast allocator.
 */
FMValue::FMValue( FMValueType type )
   : type_( type )
   , allocated_( 0 )
   , comments_( 0 )
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   , itemIsUsed_( 0 )
#endif
{
   switch ( type )
   {
   case nullFMValue:
      break;
   case intFMValue:
   case uintFMValue:
      FMvalue_.int_ = 0;
      break;
   case realFMValue:
      FMvalue_.real_ = 0.0;
      break;
   case stringFMValue:
      FMvalue_.string_ = 0;
      break;
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   case arrayFMValue:
   case objectFMValue:
      FMvalue_.map_ = new ObjectFMValues();
      break;
#else
   case arrayFMValue:
      FMvalue_.array_ = arrayAllocator()->newArray();
      break;
   case objectFMValue:
      FMvalue_.map_ = mapAllocator()->newMap();
      break;
#endif
   case booleanFMValue:
      FMvalue_.bool_ = false;
      break;
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }
}


#if defined(FMJSON_HAS_INT64)
FMValue::FMValue( UInt FMvalue )
   : type_( uintFMValue )
   , comments_( 0 )
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   , itemIsUsed_( 0 )
#endif
{
   FMvalue_.uint_ = FMvalue;
}

FMValue::FMValue( Int FMvalue )
   : type_( intFMValue )
   , comments_( 0 )
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   , itemIsUsed_( 0 )
#endif
{
   FMvalue_.int_ = FMvalue;
}

#endif // if defined(FMJSON_HAS_INT64)


FMValue::FMValue( Int64 FMvalue )
   : type_( intFMValue )
   , comments_( 0 )
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   , itemIsUsed_( 0 )
#endif
{
   FMvalue_.int_ = FMvalue;
}


FMValue::FMValue( UInt64 FMvalue )
   : type_( uintFMValue )
   , comments_( 0 )
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   , itemIsUsed_( 0 )
#endif
{
   FMvalue_.uint_ = FMvalue;
}

FMValue::FMValue( double FMvalue )
   : type_( realFMValue )
   , comments_( 0 )
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   , itemIsUsed_( 0 )
#endif
{
   FMvalue_.real_ = FMvalue;
}

FMValue::FMValue( const char *FMvalue )
   : type_( stringFMValue )
   , allocated_( true )
   , comments_( 0 )
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   , itemIsUsed_( 0 )
#endif
{
   FMvalue_.string_ = duplicateStringFMValue( FMvalue );
}


FMValue::FMValue( const char *beginFMValue, 
              const char *endFMValue )
   : type_( stringFMValue )
   , allocated_( true )
   , comments_( 0 )
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   , itemIsUsed_( 0 )
#endif
{
   FMvalue_.string_ = duplicateStringFMValue( beginFMValue, 
                                          (unsigned int)(endFMValue - beginFMValue) );
}


FMValue::FMValue( const std::string &FMvalue )
   : type_( stringFMValue )
   , allocated_( true )
   , comments_( 0 )
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   , itemIsUsed_( 0 )
#endif
{
   FMvalue_.string_ = duplicateStringFMValue( FMvalue.c_str(), 
                                          (unsigned int)FMvalue.length() );

}

FMValue::FMValue( const StaticString &FMvalue )
   : type_( stringFMValue )
   , allocated_( false )
   , comments_( 0 )
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   , itemIsUsed_( 0 )
#endif
{
   FMvalue_.string_ = const_cast<char *>( FMvalue.c_str() );
}


# ifdef FMJSON_USE_CPPTL
FMValue::FMValue( const CppTL::ConstString &FMvalue )
   : type_( stringFMValue )
   , allocated_( true )
   , comments_( 0 )
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   , itemIsUsed_( 0 )
#endif
{
   FMvalue_.string_ = duplicateStringFMValue( FMvalue, FMvalue.length() );
}
# endif

FMValue::FMValue( bool FMvalue )
   : type_( booleanFMValue )
   , comments_( 0 )
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   , itemIsUsed_( 0 )
#endif
{
   FMvalue_.bool_ = FMvalue;
}


FMValue::FMValue( const FMValue &other )
   : type_( other.type_ )
   , comments_( 0 )
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   , itemIsUsed_( 0 )
#endif
{
   switch ( type_ )
   {
   case nullFMValue:
   case intFMValue:
   case uintFMValue:
   case realFMValue:
   case booleanFMValue:
      FMvalue_ = other.FMvalue_;
      break;
   case stringFMValue:
      if ( other.FMvalue_.string_ )
      {
         FMvalue_.string_ = duplicateStringFMValue( other.FMvalue_.string_ );
         allocated_ = true;
      }
      else
         FMvalue_.string_ = 0;
      break;
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   case arrayFMValue:
   case objectFMValue:
      FMvalue_.map_ = new ObjectFMValues( *other.FMvalue_.map_ );
      break;
#else
   case arrayFMValue:
      FMvalue_.array_ = arrayAllocator()->newArrayCopy( *other.FMvalue_.array_ );
      break;
   case objectFMValue:
      FMvalue_.map_ = mapAllocator()->newMapCopy( *other.FMvalue_.map_ );
      break;
#endif
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }
   if ( other.comments_ )
   {
      comments_ = new CommentInfo[numberOfCommentPlacement];
      for ( int comment =0; comment < numberOfCommentPlacement; ++comment )
      {
         const CommentInfo &otherComment = other.comments_[comment];
         if ( otherComment.comment_ )
            comments_[comment].setComment( otherComment.comment_ );
      }
   }
}


FMValue::~FMValue()
{
   switch ( type_ )
   {
   case nullFMValue:
   case intFMValue:
   case uintFMValue:
   case realFMValue:
   case booleanFMValue:
      break;
   case stringFMValue:
      if ( allocated_ )
         releaseStringFMValue( FMvalue_.string_ );
      break;
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   case arrayFMValue:
   case objectFMValue:
	   if (FMvalue_.map_ != NULL)
	   {
		   FMvalue_.map_->clear();
		   delete FMvalue_.map_;
		   FMvalue_.map_ = NULL;
	   }
      break;
#else
   case arrayFMValue:
      arrayAllocator()->destructArray( FMvalue_.array_ );
      break;
   case objectFMValue:
      mapAllocator()->destructMap( FMvalue_.map_ );
      break;
#endif
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }

   if ( comments_ )
      delete[] comments_;
}

FMValue &
FMValue::operator=( const FMValue &other )
{
   FMValue temp( other );
   swap( temp );
   return *this;
}

void 
FMValue::swap( FMValue &other )
{
   FMValueType temp = type_;
   type_ = other.type_;
   other.type_ = temp;
   std::swap( FMvalue_, other.FMvalue_ );
   int temp2 = allocated_;
   allocated_ = other.allocated_;
   other.allocated_ = temp2;
}

FMValueType 
FMValue::type() const
{
   return type_;
}


int 
FMValue::compare( const FMValue &other ) const
{
   if ( *this < other )
      return -1;
   if ( *this > other )
      return 1;
   return 0;
}


bool 
FMValue::operator <( const FMValue &other ) const
{
   int typeDelta = type_ - other.type_;
   if ( typeDelta )
      return typeDelta < 0 ? true : false;
   switch ( type_ )
   {
   case nullFMValue:
      return false;
   case intFMValue:
      return FMvalue_.int_ < other.FMvalue_.int_;
   case uintFMValue:
      return FMvalue_.uint_ < other.FMvalue_.uint_;
   case realFMValue:
      return FMvalue_.real_ < other.FMvalue_.real_;
   case booleanFMValue:
      return FMvalue_.bool_ < other.FMvalue_.bool_;
   case stringFMValue:
      return ( FMvalue_.string_ == 0  &&  other.FMvalue_.string_ )
             || ( other.FMvalue_.string_  
                  &&  FMvalue_.string_  
                  && strcmp( FMvalue_.string_, other.FMvalue_.string_ ) < 0 );
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   case arrayFMValue:
   case objectFMValue:
      {
         int delta = int( FMvalue_.map_->size() - other.FMvalue_.map_->size() );
         if ( delta )
            return delta < 0;
         return (*FMvalue_.map_) < (*other.FMvalue_.map_);
      }
#else
   case arrayFMValue:
      return FMvalue_.array_->compare( *(other.FMvalue_.array_) ) < 0;
   case objectFMValue:
      return FMvalue_.map_->compare( *(other.FMvalue_.map_) ) < 0;
#endif
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }
   return false;  // unreachable
}

bool 
FMValue::operator <=( const FMValue &other ) const
{
   return !(other < *this);
}

bool 
FMValue::operator >=( const FMValue &other ) const
{
   return !(*this < other);
}

bool 
FMValue::operator >( const FMValue &other ) const
{
   return other < *this;
}

bool 
FMValue::operator ==( const FMValue &other ) const
{
   //if ( type_ != other.type_ )
   // GCC 2.95.3 says:
   // attempt to take address of bit-field structure member `Json::FMValue::type_'
   // Beats me, but a temp solves the problem.
   int temp = other.type_;
   if ( type_ != temp )
      return false;
   switch ( type_ )
   {
   case nullFMValue:
      return true;
   case intFMValue:
      return FMvalue_.int_ == other.FMvalue_.int_;
   case uintFMValue:
      return FMvalue_.uint_ == other.FMvalue_.uint_;
   case realFMValue:
      return FMvalue_.real_ == other.FMvalue_.real_;
   case booleanFMValue:
      return FMvalue_.bool_ == other.FMvalue_.bool_;
   case stringFMValue:
      return ( FMvalue_.string_ == other.FMvalue_.string_ )
             || ( other.FMvalue_.string_  
                  &&  FMvalue_.string_  
                  && strcmp( FMvalue_.string_, other.FMvalue_.string_ ) == 0 );
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   case arrayFMValue:
   case objectFMValue:
      return FMvalue_.map_->size() == other.FMvalue_.map_->size()
             && (*FMvalue_.map_) == (*other.FMvalue_.map_);
#else
   case arrayFMValue:
      return FMvalue_.array_->compare( *(other.FMvalue_.array_) ) == 0;
   case objectFMValue:
      return FMvalue_.map_->compare( *(other.FMvalue_.map_) ) == 0;
#endif
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }
   return false;  // unreachable
}

bool 
FMValue::operator !=( const FMValue &other ) const
{
   return !( *this == other );
}

const char *
FMValue::asCString() const
{
   FMJSON_ASSERT( type_ == stringFMValue );
   return FMvalue_.string_;
}


std::string 
FMValue::asString() const
{
   switch ( type_ )
   {
   case nullFMValue:
      return "";
   case stringFMValue:
      return FMvalue_.string_ ? FMvalue_.string_ : "";
   case booleanFMValue:
      return FMvalue_.bool_ ? "true" : "false";
   case intFMValue:
   case uintFMValue:
   case realFMValue:
   case arrayFMValue:
   case objectFMValue:
      FMJSON_FAIL_MESSAGE( "Type is not convertible to string" );
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }
   return ""; // unreachable
}

# ifdef FMJSON_USE_CPPTL
CppTL::ConstString 
FMValue::asConstString() const
{
   return CppTL::ConstString( asString().c_str() );
}
# endif


FMValue::Int 
FMValue::asInt() const
{
   switch ( type_ )
   {
   case nullFMValue:
      return 0;
   case intFMValue:
      FMJSON_ASSERT_MESSAGE( FMvalue_.int_ >= minInt  &&  FMvalue_.int_ <= maxInt, "unsigned integer out of signed int range" );
      return Int(FMvalue_.int_);
   case uintFMValue:
      FMJSON_ASSERT_MESSAGE( FMvalue_.uint_ <= UInt(maxInt), "unsigned integer out of signed int range" );
      return Int(FMvalue_.uint_);
   case realFMValue:
      FMJSON_ASSERT_MESSAGE( FMvalue_.real_ >= minInt  &&  FMvalue_.real_ <= maxInt, "Real out of signed integer range" );
      return Int( FMvalue_.real_ );
   case booleanFMValue:
      return FMvalue_.bool_ ? 1 : 0;
   case stringFMValue:
   case arrayFMValue:
   case objectFMValue:
      FMJSON_FAIL_MESSAGE( "Type is not convertible to int" );
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }
   return 0; // unreachable;
}


FMValue::UInt 
FMValue::asUInt() const
{
   switch ( type_ )
   {
   case nullFMValue:
      return 0;
   case intFMValue:
      FMJSON_ASSERT_MESSAGE( FMvalue_.int_ >= 0, "Negative integer can not be converted to unsigned integer" );
      FMJSON_ASSERT_MESSAGE( FMvalue_.int_ <= maxUInt, "signed integer out of UInt range" );
      return UInt(FMvalue_.int_);
   case uintFMValue:
      FMJSON_ASSERT_MESSAGE( FMvalue_.uint_ <= maxUInt, "unsigned integer out of UInt range" );
      return UInt(FMvalue_.uint_);
   case realFMValue:
      FMJSON_ASSERT_MESSAGE( FMvalue_.real_ >= 0  &&  FMvalue_.real_ <= maxUInt,  "Real out of unsigned integer range" );
      return UInt( FMvalue_.real_ );
   case booleanFMValue:
      return FMvalue_.bool_ ? 1 : 0;
   case stringFMValue:
   case arrayFMValue:
   case objectFMValue:
      FMJSON_FAIL_MESSAGE( "Type is not convertible to uint" );
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }
   return 0; // unreachable;
}


# if defined(FMJSON_HAS_INT64)

FMValue::Int64
FMValue::asInt64() const
{
   switch ( type_ )
   {
   case nullFMValue:
      return 0;
   case intFMValue:
      return FMvalue_.int_;
   case uintFMValue:
      FMJSON_ASSERT_MESSAGE( FMvalue_.uint_ <= UInt64(maxInt64), "unsigned integer out of Int64 range" );
      return FMvalue_.uint_;
   case realFMValue:
      FMJSON_ASSERT_MESSAGE( FMvalue_.real_ >= minInt64  &&  FMvalue_.real_ <= maxInt64, "Real out of Int64 range" );
      return Int( FMvalue_.real_ );
   case booleanFMValue:
      return FMvalue_.bool_ ? 1 : 0;
   case stringFMValue:
   case arrayFMValue:
   case objectFMValue:
      FMJSON_FAIL_MESSAGE( "Type is not convertible to Int64" );
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }
   return 0; // unreachable;
}


FMValue::UInt64
FMValue::asUInt64() const
{
   switch ( type_ )
   {
   case nullFMValue:
      return 0;
   case intFMValue:
      FMJSON_ASSERT_MESSAGE( FMvalue_.int_ >= 0, "Negative integer can not be converted to UInt64" );
      return FMvalue_.int_;
   case uintFMValue:
      return FMvalue_.uint_;
   case realFMValue:
      FMJSON_ASSERT_MESSAGE( FMvalue_.real_ >= 0  &&  FMvalue_.real_ <= maxUInt64,  "Real out of UInt64 range" );
      return UInt( FMvalue_.real_ );
   case booleanFMValue:
      return FMvalue_.bool_ ? 1 : 0;
   case stringFMValue:
   case arrayFMValue:
   case objectFMValue:
      FMJSON_FAIL_MESSAGE( "Type is not convertible to UInt64" );
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }
   return 0; // unreachable;
}
# endif // if defined(FMJSON_HAS_INT64)


LargestInt 
FMValue::asLargestInt() const
{
#if defined(FMJSON_NO_INT64)
    return asInt();
#else
    return asInt64();
#endif
}


LargestUInt 
FMValue::asLargestUInt() const
{
#if defined(FMJSON_NO_INT64)
    return asUInt();
#else
    return asUInt64();
#endif
}


double 
FMValue::asDouble() const
{
   switch ( type_ )
   {
   case nullFMValue:
      return 0.0;
   case intFMValue:
      return static_cast<double>( FMvalue_.int_ );
   case uintFMValue:
#if !defined(FMJSON_USE_INT64_DOUBLE_CONVERSION)
      return static_cast<double>( FMvalue_.uint_ );
#else // if !defined(FMJSON_USE_INT64_DOUBLE_CONVERSION)
      return static_cast<double>( Int(FMvalue_.uint_/2) ) * 2 + Int(FMvalue_.uint_ & 1);
#endif // if !defined(FMJSON_USE_INT64_DOUBLE_CONVERSION)
   case realFMValue:
      return FMvalue_.real_;
   case booleanFMValue:
      return FMvalue_.bool_ ? 1.0 : 0.0;
   case stringFMValue:
   case arrayFMValue:
   case objectFMValue:
      FMJSON_FAIL_MESSAGE( "Type is not convertible to double" );
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }
   return 0; // unreachable;
}

float
FMValue::asFloat() const
{
   switch ( type_ )
   {
   case nullFMValue:
      return 0.0f;
   case intFMValue:
      return static_cast<float>( FMvalue_.int_ );
   case uintFMValue:
#if !defined(FMJSON_USE_INT64_DOUBLE_CONVERSION)
      return static_cast<float>( FMvalue_.uint_ );
#else // if !defined(FMJSON_USE_INT64_DOUBLE_CONVERSION)
      return static_cast<float>( Int(FMvalue_.uint_/2) ) * 2 + Int(FMvalue_.uint_ & 1);
#endif // if !defined(FMJSON_USE_INT64_DOUBLE_CONVERSION)
   case realFMValue:
      return static_cast<float>( FMvalue_.real_ );
   case booleanFMValue:
      return FMvalue_.bool_ ? 1.0f : 0.0f;
   case stringFMValue:
   case arrayFMValue:
   case objectFMValue:
      FMJSON_FAIL_MESSAGE( "Type is not convertible to float" );
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }
   return 0.0f; // unreachable;
}

bool 
FMValue::asBool() const
{
   switch ( type_ )
   {
   case nullFMValue:
      return false;
   case intFMValue:
   case uintFMValue:
      return FMvalue_.int_ != 0;
   case realFMValue:
      return FMvalue_.real_ != 0.0;
   case booleanFMValue:
      return FMvalue_.bool_;
   case stringFMValue:
      return FMvalue_.string_  &&  FMvalue_.string_[0] != 0;
   case arrayFMValue:
   case objectFMValue:
      return FMvalue_.map_->size() != 0;
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }
   return false; // unreachable;
}


bool 
FMValue::isConvertibleTo( FMValueType other ) const
{
   switch ( type_ )
   {
   case nullFMValue:
      return true;
   case intFMValue:
      return ( other == nullFMValue  &&  FMvalue_.int_ == 0 )
             || other == intFMValue
             || ( other == uintFMValue  && FMvalue_.int_ >= 0 )
             || other == realFMValue
             || other == stringFMValue
             || other == booleanFMValue;
   case uintFMValue:
      return ( other == nullFMValue  &&  FMvalue_.uint_ == 0 )
             || ( other == intFMValue  && FMvalue_.uint_ <= (unsigned)maxInt )
             || other == uintFMValue
             || other == realFMValue
             || other == stringFMValue
             || other == booleanFMValue;
   case realFMValue:
      return ( other == nullFMValue  &&  FMvalue_.real_ == 0.0 )
             || ( other == intFMValue  &&  FMvalue_.real_ >= minInt  &&  FMvalue_.real_ <= maxInt )
             || ( other == uintFMValue  &&  FMvalue_.real_ >= 0  &&  FMvalue_.real_ <= maxUInt )
             || other == realFMValue
             || other == stringFMValue
             || other == booleanFMValue;
   case booleanFMValue:
      return ( other == nullFMValue  &&  FMvalue_.bool_ == false )
             || other == intFMValue
             || other == uintFMValue
             || other == realFMValue
             || other == stringFMValue
             || other == booleanFMValue;
   case stringFMValue:
      return other == stringFMValue
             || ( other == nullFMValue  &&  (!FMvalue_.string_  ||  FMvalue_.string_[0] == 0) );
   case arrayFMValue:
      return other == arrayFMValue
             ||  ( other == nullFMValue  &&  FMvalue_.map_->size() == 0 );
   case objectFMValue:
      return other == objectFMValue
             ||  ( other == nullFMValue  &&  FMvalue_.map_->size() == 0 );
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }
   return false; // unreachable;
}


/// Number of FMvalues in array or object
ArrayIndex 
FMValue::size() const
{
   switch ( type_ )
   {
   case nullFMValue:
   case intFMValue:
   case uintFMValue:
   case realFMValue:
   case booleanFMValue:
   case stringFMValue:
      return 0;
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   case arrayFMValue:  // size of the array is highest index + 1
      if ( !FMvalue_.map_->empty() )
      {
         ObjectFMValues::const_iterator itLast = FMvalue_.map_->end();
         --itLast;
         return (*itLast).first.index()+1;
      }
      return 0;
   case objectFMValue:
      return ArrayIndex( FMvalue_.map_->size() );
#else
   case arrayFMValue:
      return Int( FMvalue_.array_->size() );
   case objectFMValue:
      return Int( FMvalue_.map_->size() );
#endif
   default:
      FMJSON_ASSERT_UNREACHABLE;
   }
   return 0; // unreachable;
}


bool 
FMValue::empty() const
{
   if ( isNull() || isArray() || isObject() )
      return size() == 0u;
   else
      return false;
}


bool
FMValue::operator!() const
{
   return isNull();
}


void 
FMValue::clear()
{
   FMJSON_ASSERT( type_ == nullFMValue  ||  type_ == arrayFMValue  || type_ == objectFMValue );

   switch ( type_ )
   {
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   case arrayFMValue:
   case objectFMValue:
    //  FMvalue_.map_->clear();
       if (FMvalue_.map_ != NULL)
       {
           FMvalue_.map_->clear();
           delete FMvalue_.map_;
           FMvalue_.map_ = NULL;
       }
	   if (FMvalue_.string_ != NULL)
	   {
		   delete FMvalue_.string_;
		   FMvalue_.string_ = NULL;
	   }
      break;
#else
   case arrayFMValue:
      FMvalue_.array_->clear();
      break;
   case objectFMValue:
      FMvalue_.map_->clear();
      break;
#endif
   default:
      break;
   }
}

void 
FMValue::resize( ArrayIndex newSize )
{
   FMJSON_ASSERT( type_ == nullFMValue  ||  type_ == arrayFMValue );
   if ( type_ == nullFMValue )
      *this = FMValue( arrayFMValue );
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   ArrayIndex oldSize = size();
   if ( newSize == 0 )
      clear();
   else if ( newSize > oldSize )
      (*this)[ newSize - 1 ];
   else
   {
      for ( ArrayIndex index = newSize; index < oldSize; ++index )
      {
         FMvalue_.map_->erase( index );
      }
      assert( size() == newSize );
   }
#else
   FMvalue_.array_->resize( newSize );
#endif
}


FMValue &
FMValue::operator[]( ArrayIndex index )
{
   FMJSON_ASSERT( type_ == nullFMValue  ||  type_ == arrayFMValue );
   if ( type_ == nullFMValue )
      *this = FMValue( arrayFMValue );
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   CZString key( index );
   ObjectFMValues::iterator it = FMvalue_.map_->lower_bound( key );
   if ( it != FMvalue_.map_->end()  &&  (*it).first == key )
      return (*it).second;

   ObjectFMValues::value_type defaultValue( key, jsonNull );
   it = FMvalue_.map_->insert( it, defaultValue );
   return (*it).second;
#else
   return FMvalue_.array_->resolveReference( index );
#endif
}


FMValue &
FMValue::operator[]( int index )
{
   FMJSON_ASSERT( index >= 0 );
   return (*this)[ ArrayIndex(index) ];
}


const FMValue &
FMValue::operator[]( ArrayIndex index ) const
{
   FMJSON_ASSERT( type_ == nullFMValue  ||  type_ == arrayFMValue );
   if ( type_ == nullFMValue )
      return jsonNull;
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   CZString key( index );
   ObjectFMValues::const_iterator it = FMvalue_.map_->find( key );
   if ( it == FMvalue_.map_->end() )
      return jsonNull;
   return (*it).second;
#else
   FMValue *FMvalue = FMvalue_.array_->find( index );
   return FMvalue ? *FMvalue : null;
#endif
}


const FMValue &
FMValue::operator[]( int index ) const
{
   FMJSON_ASSERT( index >= 0 );
   return (*this)[ ArrayIndex(index) ];
}


FMValue &
FMValue::operator[]( const char *key )
{
   return resolveReference( key, false );
}


FMValue &
FMValue::resolveReference( const char *key, 
                         bool isStatic )
{
   FMJSON_ASSERT( type_ == nullFMValue  ||  type_ == objectFMValue );
   if ( type_ == nullFMValue )
      *this = FMValue( objectFMValue );
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   CZString actualKey( key, isStatic ? CZString::noDuplication 
                                     : CZString::duplicateOnCopy );
   ObjectFMValues::iterator it = FMvalue_.map_->lower_bound( actualKey );
   if ( it != FMvalue_.map_->end()  &&  (*it).first == actualKey )
      return (*it).second;

   ObjectFMValues::value_type defaultValue( actualKey, jsonNull );
   it = FMvalue_.map_->insert( it, defaultValue );
   FMValue &FMvalue = (*it).second;
   return FMvalue;
#else
   return FMvalue_.map_->resolveReference( key, isStatic );
#endif
}


FMValue 
FMValue::get( ArrayIndex index, 
            const FMValue &defaultFMValue ) const
{
   const FMValue *FMvalue = &((*this)[index]);
   return FMvalue == &jsonNull ? defaultFMValue : *FMvalue;
}


bool 
FMValue::isValidIndex( ArrayIndex index ) const
{
   return index < size();
}



const FMValue &
FMValue::operator[]( const char *key ) const
{
   FMJSON_ASSERT( type_ == nullFMValue  ||  type_ == objectFMValue );
   if ( type_ == nullFMValue )
      return jsonNull;
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   CZString actualKey( key, CZString::noDuplication );
   ObjectFMValues::const_iterator it = FMvalue_.map_->find( actualKey );
   if ( it == FMvalue_.map_->end() )
      return jsonNull;
   return (*it).second;
#else
   const FMValue *FMvalue = FMvalue_.map_->find( key );
   return FMvalue ? *FMvalue : null;
#endif
}


FMValue &
FMValue::operator[]( const std::string &key )
{
   return (*this)[ key.c_str() ];
}


const FMValue &
FMValue::operator[]( const std::string &key ) const
{
   return (*this)[ key.c_str() ];
}

FMValue &
FMValue::operator[]( const StaticString &key )
{
   return resolveReference( key, true );
}


# ifdef FMJSON_USE_CPPTL
FMValue &
FMValue::operator[]( const CppTL::ConstString &key )
{
   return (*this)[ key.c_str() ];
}


const FMValue &
FMValue::operator[]( const CppTL::ConstString &key ) const
{
   return (*this)[ key.c_str() ];
}
# endif


FMValue &
FMValue::append( const FMValue &FMvalue )
{
   return (*this)[size()] = FMvalue;
}


FMValue 
FMValue::get( const char *key, 
            const FMValue &defaultFMValue ) const
{
   const FMValue *FMvalue = &((*this)[key]);
   return FMvalue == &jsonNull ? defaultFMValue : *FMvalue;
}


FMValue 
FMValue::get( const std::string &key,
            const FMValue &defaultFMValue ) const
{
   return get( key.c_str(), defaultFMValue );
}

FMValue
FMValue::removeMember( const char* key )
{
   FMJSON_ASSERT( type_ == nullFMValue  ||  type_ == objectFMValue );
   if ( type_ == nullFMValue )
      return jsonNull;
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   CZString actualKey( key, CZString::noDuplication );
   ObjectFMValues::iterator it = FMvalue_.map_->find( actualKey );
   if ( it == FMvalue_.map_->end() )
      return jsonNull;
   FMValue old(it->second);
   FMvalue_.map_->erase(it);
   return old;
#else
   FMValue *FMvalue = FMvalue_.map_->find( key );
   if (FMvalue){
      FMValue old(*FMvalue);
      FMvalue_.map_.remove( key );
      return old;
   } else {
      return null;
   }
#endif
}

FMValue
FMValue::removeMember( const std::string &key )
{
   return removeMember( key.c_str() );
}

# ifdef FMJSON_USE_CPPTL
FMValue 
FMValue::get( const CppTL::ConstString &key,
            const FMValue &defaultFMValue ) const
{
   return get( key.c_str(), defaultFMValue );
}
# endif

bool 
FMValue::isMember( const char *key ) const
{
   const FMValue *FMvalue = &((*this)[key]);
   return FMvalue != &jsonNull;
}


bool 
FMValue::isMember( const std::string &key ) const
{
   return isMember( key.c_str() );
}


# ifdef FMJSON_USE_CPPTL
bool 
FMValue::isMember( const CppTL::ConstString &key ) const
{
   return isMember( key.c_str() );
}
#endif

FMValue::Members 
FMValue::getMemberNames() const
{
   FMJSON_ASSERT( type_ == nullFMValue  ||  type_ == objectFMValue );
   if ( type_ == nullFMValue )
       return FMValue::Members();
   Members members;
   members.reserve( FMvalue_.map_->size() );
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
   ObjectFMValues::const_iterator it = FMvalue_.map_->begin();
   ObjectFMValues::const_iterator itEnd = FMvalue_.map_->end();
   for ( ; it != itEnd; ++it )
      members.push_back( std::string( (*it).first.c_str() ) );
#else
   FMValueInternalMap::IteratorState it;
   FMValueInternalMap::IteratorState itEnd;
   FMvalue_.map_->makeBeginIterator( it );
   FMvalue_.map_->makeEndIterator( itEnd );
   for ( ; !FMValueInternalMap::equals( it, itEnd ); FMValueInternalMap::increment(it) )
      members.push_back( std::string( FMValueInternalMap::key( it ) ) );
#endif
   return members;
}
//
//# ifdef FMJSON_USE_CPPTL
//EnumMemberNames
//FMValue::enumMemberNames() const
//{
//   if ( type_ == objectFMValue )
//   {
//      return CppTL::Enum::any(  CppTL::Enum::transform(
//         CppTL::Enum::keys( *(FMvalue_.map_), CppTL::Type<const CZString &>() ),
//         MemberNamesTransform() ) );
//   }
//   return EnumMemberNames();
//}
//
//
//EnumFMValues 
//FMValue::enumFMValues() const
//{
//   if ( type_ == objectFMValue  ||  type_ == arrayFMValue )
//      return CppTL::Enum::anyFMValues( *(FMvalue_.map_), 
//                                     CppTL::Type<const FMValue &>() );
//   return EnumFMValues();
//}
//
//# endif


bool
FMValue::isNull() const
{
   return type_ == nullFMValue;
}


bool 
FMValue::isBool() const
{
   return type_ == booleanFMValue;
}


bool
FMValue::isInt() const
{
   return type_ == intFMValue;
}


bool 
FMValue::isUInt() const
{
   return type_ == uintFMValue;
}


bool 
FMValue::isIntegral() const
{
   return type_ == intFMValue  
          ||  type_ == uintFMValue  
          ||  type_ == booleanFMValue;
}


bool 
FMValue::isDouble() const
{
   return type_ == realFMValue;
}


bool 
FMValue::isNumeric() const
{
   return isIntegral() || isDouble();
}


bool 
FMValue::isString() const
{
   return type_ == stringFMValue;
}


bool 
FMValue::isArray() const
{
   return type_ == nullFMValue  ||  type_ == arrayFMValue;
}


bool 
FMValue::isObject() const
{
   return type_ == nullFMValue  ||  type_ == objectFMValue;
}


void 
FMValue::setComment( const char *comment,
                   CommentPlacement placement )
{
   if ( !comments_ )
      comments_ = new CommentInfo[numberOfCommentPlacement];
   comments_[placement].setComment( comment );
}


void 
FMValue::setComment( const std::string &comment,
                   CommentPlacement placement )
{
   setComment( comment.c_str(), placement );
}


bool 
FMValue::hasComment( CommentPlacement placement ) const
{
   return comments_ != 0  &&  comments_[placement].comment_ != 0;
}

std::string 
FMValue::getComment( CommentPlacement placement ) const
{
   if ( hasComment(placement) )
      return comments_[placement].comment_;
   return "";
}


std::string 
FMValue::toStyledString() const
{
   StyledWriter writer;
   return writer.write( *this );
}


FMValue::const_iterator 
FMValue::begin() const
{
   switch ( type_ )
   {
#ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   case arrayFMValue:
      if ( FMvalue_.array_ )
      {
         FMValueInternalArray::IteratorState it;
         FMvalue_.array_->makeBeginIterator( it );
         return const_iterator( it );
      }
      break;
   case objectFMValue:
      if ( FMvalue_.map_ )
      {
         FMValueInternalMap::IteratorState it;
         FMvalue_.map_->makeBeginIterator( it );
         return const_iterator( it );
      }
      break;
#else
   case arrayFMValue:
   case objectFMValue:
      if ( FMvalue_.map_ )
         return const_iterator( FMvalue_.map_->begin() );
      break;
#endif
   default:
      break;
   }
   return const_iterator();
}

FMValue::const_iterator 
FMValue::end() const
{
   switch ( type_ )
   {
#ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   case arrayFMValue:
      if ( FMvalue_.array_ )
      {
         FMValueInternalArray::IteratorState it;
         FMvalue_.array_->makeEndIterator( it );
         return const_iterator( it );
      }
      break;
   case objectFMValue:
      if ( FMvalue_.map_ )
      {
         FMValueInternalMap::IteratorState it;
         FMvalue_.map_->makeEndIterator( it );
         return const_iterator( it );
      }
      break;
#else
   case arrayFMValue:
   case objectFMValue:
      if ( FMvalue_.map_ )
         return const_iterator( FMvalue_.map_->end() );
      break;
#endif
   default:
      break;
   }
   return const_iterator();
}


FMValue::iterator 
FMValue::begin()
{
   switch ( type_ )
   {
#ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   case arrayFMValue:
      if ( FMvalue_.array_ )
      {
         FMValueInternalArray::IteratorState it;
         FMvalue_.array_->makeBeginIterator( it );
         return iterator( it );
      }
      break;
   case objectFMValue:
      if ( FMvalue_.map_ )
      {
         FMValueInternalMap::IteratorState it;
         FMvalue_.map_->makeBeginIterator( it );
         return iterator( it );
      }
      break;
#else
   case arrayFMValue:
   case objectFMValue:
      if ( FMvalue_.map_ )
         return iterator( FMvalue_.map_->begin() );
      break;
#endif
   default:
      break;
   }
   return iterator();
}

FMValue::iterator 
FMValue::end()
{
   switch ( type_ )
   {
#ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   case arrayFMValue:
      if ( FMvalue_.array_ )
      {
         FMValueInternalArray::IteratorState it;
         FMvalue_.array_->makeEndIterator( it );
         return iterator( it );
      }
      break;
   case objectFMValue:
      if ( FMvalue_.map_ )
      {
         FMValueInternalMap::IteratorState it;
         FMvalue_.map_->makeEndIterator( it );
         return iterator( it );
      }
      break;
#else
   case arrayFMValue:
   case objectFMValue:
      if ( FMvalue_.map_ )
         return iterator( FMvalue_.map_->end() );
      break;
#endif
   default:
      break;
   }
   return iterator();
}


// class PathArgument
// //////////////////////////////////////////////////////////////////

PathArgument::PathArgument()
   : kind_( kindNone )
{
}


PathArgument::PathArgument( ArrayIndex index )
   : index_( index )
   , kind_( kindIndex )
{
}


PathArgument::PathArgument( const char *key )
   : key_( key )
   , kind_( kindKey )
{
}


PathArgument::PathArgument( const std::string &key )
   : key_( key.c_str() )
   , kind_( kindKey )
{
}

// class Path
// //////////////////////////////////////////////////////////////////

Path::Path( const std::string &path,
            const PathArgument &a1,
            const PathArgument &a2,
            const PathArgument &a3,
            const PathArgument &a4,
            const PathArgument &a5 )
{
   InArgs in;
   in.push_back( &a1 );
   in.push_back( &a2 );
   in.push_back( &a3 );
   in.push_back( &a4 );
   in.push_back( &a5 );
   makePath( path, in );
}


void 
Path::makePath( const std::string &path,
                const InArgs &in )
{
   const char *current = path.c_str();
   const char *end = current + path.length();
   InArgs::const_iterator itInArg = in.begin();
   while ( current != end )
   {
      if ( *current == '[' )
      {
         ++current;
         if ( *current == '%' )
            addPathInArg( path, in, itInArg, PathArgument::kindIndex );
         else
         {
            ArrayIndex index = 0;
            for ( ; current != end && *current >= '0'  &&  *current <= '9'; ++current )
               index = index * 10 + ArrayIndex(*current - '0');
            args_.push_back( index );
         }
         if ( current == end  ||  *current++ != ']' )
            invalidPath( path, int(current - path.c_str()) );
      }
      else if ( *current == '%' )
      {
         addPathInArg( path, in, itInArg, PathArgument::kindKey );
         ++current;
      }
      else if ( *current == '.' )
      {
         ++current;
      }
      else
      {
         const char *beginName = current;
         while ( current != end  &&  !strchr( "[.", *current ) )
            ++current;
         args_.push_back( std::string( beginName, current ) );
      }
   }
}


void 
Path::addPathInArg( const std::string &path, 
                    const InArgs &in, 
                    InArgs::const_iterator &itInArg, 
                    PathArgument::Kind kind )
{
   if ( itInArg == in.end() )
   {
      // Error: missing argument %d
   }
   else if ( (*itInArg)->kind_ != kind )
   {
      // Error: bad argument type
   }
   else
   {
      args_.push_back( **itInArg );
   }
}


void 
Path::invalidPath( const std::string &path, 
                   int location )
{
   // Error: invalid path.
}


const FMValue &
Path::resolve( const FMValue &root ) const
{
   const FMValue *node = &root;
   for ( Args::const_iterator it = args_.begin(); it != args_.end(); ++it )
   {
      const PathArgument &arg = *it;
      if ( arg.kind_ == PathArgument::kindIndex )
      {
         if ( !node->isArray()  ||  node->isValidIndex( arg.index_ ) )
         {
            // Error: unable to resolve path (array FMvalue expected at position...
         }
         node = &((*node)[arg.index_]);
      }
      else if ( arg.kind_ == PathArgument::kindKey )
      {
         if ( !node->isObject() )
         {
            // Error: unable to resolve path (object FMvalue expected at position...)
         }
         node = &((*node)[arg.key_]);
         if ( node == &FMValue::jsonNull )
         {
            // Error: unable to resolve path (object has no member named '' at position...)
         }
      }
   }
   return *node;
}


FMValue 
Path::resolve( const FMValue &root, 
               const FMValue &defaultFMValue ) const
{
   const FMValue *node = &root;
   for ( Args::const_iterator it = args_.begin(); it != args_.end(); ++it )
   {
      const PathArgument &arg = *it;
      if ( arg.kind_ == PathArgument::kindIndex )
      {
         if ( !node->isArray()  ||  node->isValidIndex( arg.index_ ) )
            return defaultFMValue;
         node = &((*node)[arg.index_]);
      }
      else if ( arg.kind_ == PathArgument::kindKey )
      {
         if ( !node->isObject() )
            return defaultFMValue;
         node = &((*node)[arg.key_]);
         if ( node == &FMValue::jsonNull )
            return defaultFMValue;
      }
   }
   return *node;
}


FMValue &
Path::make( FMValue &root ) const
{
   FMValue *node = &root;
   for ( Args::const_iterator it = args_.begin(); it != args_.end(); ++it )
   {
      const PathArgument &arg = *it;
      if ( arg.kind_ == PathArgument::kindIndex )
      {
         if ( !node->isArray() )
         {
            // Error: node is not an array at position ...
         }
         node = &((*node)[arg.index_]);
      }
      else if ( arg.kind_ == PathArgument::kindKey )
      {
         if ( !node->isObject() )
         {
            // Error: node is not an object at position...
         }
         node = &((*node)[arg.key_]);
      }
   }
   return *node;
}


} // namespace CFMJson
