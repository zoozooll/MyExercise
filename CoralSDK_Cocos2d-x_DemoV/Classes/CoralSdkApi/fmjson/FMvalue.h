// Copyright 2007-2010 Baptiste Lepilleur
// Distributed under MIT license, or public domain if desired and
// recognized in your jurisdiction.
// See file LICENSE for detail or copy at http://jsoncpp.sourceforge.net/LICENSE

#ifndef FMCPPTL_FMJSON_H_INCLUDED
# define FMCPPTL_FMJSON_H_INCLUDED

#if !defined(FMJSON_IS_AMALGAMATION)
# include "FMforwards.h"
#endif // if !defined(FMJSON_IS_AMALGAMATION)
# include <string>
# include <vector>

# ifndef FMJSON_USE_FMCPPTL_SMALLMAP
#  include <map>
# else
#  include <cpptl/smallmap.h>
# endif
# ifdef FMJSON_USE_CPPTL
#  include <cpptl/forwards.h>
# endif

/** \brief JSON (JavaScript Object Notation).
 */
namespace CFMJson {

   /** \brief Type of the FMvalue held by a FMValue object.
    */
   enum FMValueType
   {
      nullFMValue = 0, ///< 'null' FMvalue
      intFMValue,      ///< signed integer FMvalue
      uintFMValue,     ///< unsigned integer FMvalue
      realFMValue,     ///< double FMvalue
      stringFMValue,   ///< UTF-8 string FMvalue
      booleanFMValue,  ///< bool FMvalue
      arrayFMValue,    ///< array FMvalue (ordered list)
      objectFMValue    ///< object FMvalue (collection of name/FMvalue pairs).
   };

   enum CommentPlacement
   {
      commentBefore = 0,        ///< a comment placed on the line before a FMvalue
      commentAfterOnSameLine,   ///< a comment just after a FMvalue on the same line
      commentAfter,             ///< a comment on the line after a FMvalue (only make sense for root FMvalue)
      numberOfCommentPlacement
   };

//# ifdef FMJSON_USE_CPPTL
//   typedef CppTL::AnyEnumerator<const char *> EnumMemberNames;
//   typedef CppTL::AnyEnumerator<const FMValue &> EnumFMValues;
//# endif

   /** \brief Lightweight wrapper to tag static string.
    *
    * FMValue constructor and objectFMValue member assignement takes advantage of the
    * StaticString and avoid the cost of string duplication when storing the
    * string or the member name.
    *
    * Example of usage:
    * \code
    * CFMJson::FMValue aFMValue( StaticString("some text") );
    * CFMJson::FMValue object;
    * static const StaticString code("code");
    * object[code] = 1234;
    * \endcode
    */
   class FMJSON_API StaticString
   {
   public:
      explicit StaticString( const char *czstring )
         : str_( czstring )
      {
      }

      operator const char *() const
      {
         return str_;
      }

      const char *c_str() const
      {
         return str_;
      }

   private:
      const char *str_;
   };

   /** \brief Represents a <a HREF="http://www.json.org">JSON</a> FMvalue.
    *
    * This class is a discriminated union wrapper that can represents a:
    * - signed integer [range: FMValue::minInt - FMValue::maxInt]
    * - unsigned integer (range: 0 - FMValue::maxUInt)
    * - double
    * - UTF-8 string
    * - boolean
    * - 'null'
    * - an ordered list of FMValue
    * - collection of name/FMvalue pairs (javascript object)
    *
    * The type of the held FMvalue is represented by a #FMValueType and 
    * can be obtained using type().
    *
    * FMvalues of an #objectFMValue or #arrayFMValue can be accessed using operator[]() methods. 
    * Non const methods will automatically create the a #nullFMValue element 
    * if it does not exist. 
    * The sequence of an #arrayFMValue will be automatically resize and initialized 
    * with #nullFMValue. resize() can be used to enlarge or truncate an #arrayFMValue.
    *
    * The get() methods can be used to obtanis default FMvalue in the case the required element
    * does not exist.
    *
    * It is possible to iterate over the list of a #objectFMValue FMvalues using 
    * the getMemberNames() method.
    */
   class FMJSON_API FMValue 
   {
      friend class FMValueIteratorBase;
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
      friend class FMValueInternalLink;
      friend class FMValueInternalMap;
# endif
   public:
      typedef std::vector<std::string> Members;
      typedef FMValueIterator iterator;
      typedef FMValueConstIterator const_iterator;
      typedef CFMJson::UInt UInt;
      typedef CFMJson::Int Int;
# if defined(FMJSON_HAS_INT64)
      typedef CFMJson::UInt64 UInt64;
      typedef CFMJson::Int64 Int64;
#endif // defined(FMJSON_HAS_INT64)
      typedef CFMJson::LargestInt LargestInt;
      typedef CFMJson::LargestUInt LargestUInt;
      typedef CFMJson::ArrayIndex ArrayIndex;

      static const FMValue jsonNull;
      /// Minimum signed integer FMvalue that can be stored in a CFMJson::FMValue.
      static const LargestInt minLargestInt;
      /// Maximum signed integer FMvalue that can be stored in a CFMJson::FMValue.
      static const LargestInt maxLargestInt;
      /// Maximum unsigned integer FMvalue that can be stored in a CFMJson::FMValue.
      static const LargestUInt maxLargestUInt;

      /// Minimum signed int FMvalue that can be stored in a CFMJson::FMValue.
      static const Int minInt;
      /// Maximum signed int FMvalue that can be stored in a CFMJson::FMValue.
      static const Int maxInt;
      /// Maximum unsigned int FMvalue that can be stored in a CFMJson::FMValue.
      static const UInt maxUInt;

      /// Minimum signed 64 bits int FMvalue that can be stored in a CFMJson::FMValue.
      static const Int64 minInt64;
      /// Maximum signed 64 bits int FMvalue that can be stored in a CFMJson::FMValue.
      static const Int64 maxInt64;
      /// Maximum unsigned 64 bits int FMvalue that can be stored in a CFMJson::FMValue.
      static const UInt64 maxUInt64;

   private:
#ifndef FMJSONCPP_DOC_EXCLUDE_IMPLEMENTATION
# ifndef FMJSON_VALUE_USE_INTERNAL_MAP
      class CZString 
      {
      public:
         enum DuplicationPolicy 
         {
            noDuplication = 0,
            duplicate,
            duplicateOnCopy
         };
         CZString( ArrayIndex index );
         CZString( const char *cstr, DuplicationPolicy allocate );
         CZString( const CZString &other );
         ~CZString();
         CZString &operator =( const CZString &other );
         bool operator<( const CZString &other ) const;
         bool operator==( const CZString &other ) const;
         ArrayIndex index() const;
         const char *c_str() const;
         bool isStaticString() const;
      private:
         void swap( CZString &other );
         const char *cstr_;
         ArrayIndex index_;
      };

   public:
#  ifndef FMJSON_USE_FMCPPTL_SMALLMAP
      typedef std::map<CZString, FMValue> ObjectFMValues;
#  else
      typedef CppTL::SmallMap<CZString, FMValue> ObjectFMValues;
#  endif // ifndef FMJSON_USE_FMCPPTL_SMALLMAP
# endif // ifndef FMJSON_VALUE_USE_INTERNAL_MAP
#endif // ifndef FMJSONCPP_DOC_EXCLUDE_IMPLEMENTATION

   public:
      /** \brief Create a default FMValue of the given type.

        This is a very useful constructor.
        To create an empty array, pass arrayFMValue.
        To create an empty object, pass objectFMValue.
        Another FMValue can then be set to this one by assignment.
    This is useful since clear() and resize() will not alter types.

        Examples:
    \code
    CFMJson::FMValue null_FMvalue; // null
    CFMJson::FMValue arr_FMvalue(Json::arrayFMValue); // []
    CFMJson::FMValue obj_FMvalue(Json::objectFMValue); // {}
    \endcode
      */
      FMValue( FMValueType type = nullFMValue );
      FMValue( Int FMvalue );
      FMValue( UInt FMvalue );
#if defined(FMJSON_HAS_INT64)
      FMValue( Int64 FMvalue );
      FMValue( UInt64 FMvalue );
#endif // if defined(FMJSON_HAS_INT64)
      FMValue( double FMvalue );
      FMValue( const char *FMvalue );
      FMValue( const char *beginFMValue, const char *endFMValue );
      /** \brief Constructs a FMvalue from a static string.

       * Like other FMvalue string constructor but do not duplicate the string for
       * internal storage. The given string must remain alive after the call to this
       * constructor.
       * Example of usage:
       * \code
       * CFMJson::FMValue aFMValue( StaticString("some text") );
       * \endcode
       */
      FMValue( const StaticString &FMvalue );
      FMValue( const std::string &FMvalue );
# ifdef FMJSON_USE_CPPTL
      FMValue( const CppTL::ConstString &FMvalue );
# endif
      FMValue( bool FMvalue );
      FMValue( const FMValue &other );
      ~FMValue();

      FMValue &operator=( const FMValue &other );
      /// Swap FMvalues.
      /// \note Currently, comments are intentionally not swapped, for
      /// both logic and efficiency.
      void swap( FMValue &other );

      FMValueType type() const;

      bool operator <( const FMValue &other ) const;
      bool operator <=( const FMValue &other ) const;
      bool operator >=( const FMValue &other ) const;
      bool operator >( const FMValue &other ) const;

      bool operator ==( const FMValue &other ) const;
      bool operator !=( const FMValue &other ) const;

      int compare( const FMValue &other ) const;

      const char *asCString() const;
      std::string asString() const;
# ifdef FMJSON_USE_CPPTL
      CppTL::ConstString asConstString() const;
# endif
      Int asInt() const;
      UInt asUInt() const;
      Int64 asInt64() const;
      UInt64 asUInt64() const;
      LargestInt asLargestInt() const;
      LargestUInt asLargestUInt() const;
      float asFloat() const;
      double asDouble() const;
      bool asBool() const;

      bool isNull() const;
      bool isBool() const;
      bool isInt() const;
      bool isUInt() const;
      bool isIntegral() const;
      bool isDouble() const;
      bool isNumeric() const;
      bool isString() const;
      bool isArray() const;
      bool isObject() const;

      bool isConvertibleTo( FMValueType other ) const;

      /// Number of FMvalues in array or object
      ArrayIndex size() const;

      /// \brief Return true if empty array, empty object, or null;
      /// otherwise, false.
      bool empty() const;

      /// Return isNull()
      bool operator!() const;

      /// Remove all object members and array elements.
      /// \pre type() is arrayFMValue, objectFMValue, or nullFMValue
      /// \post type() is unchanged
      void clear();

      /// Resize the array to size elements. 
      /// New elements are initialized to null.
      /// May only be called on nullFMValue or arrayFMValue.
      /// \pre type() is arrayFMValue or nullFMValue
      /// \post type() is arrayFMValue
      void resize( ArrayIndex size );

      /// Access an array element (zero based index ).
      /// If the array contains less than index element, then null FMvalue are inserted
      /// in the array so that its size is index+1.
      /// (You may need to say 'FMvalue[0u]' to get your compiler to distinguish
      ///  this from the operator[] which takes a string.)
      FMValue &operator[]( ArrayIndex index );

      /// Access an array element (zero based index ).
      /// If the array contains less than index element, then null FMvalue are inserted
      /// in the array so that its size is index+1.
      /// (You may need to say 'FMvalue[0u]' to get your compiler to distinguish
      ///  this from the operator[] which takes a string.)
      FMValue &operator[]( int index );

      /// Access an array element (zero based index )
      /// (You may need to say 'FMvalue[0u]' to get your compiler to distinguish
      ///  this from the operator[] which takes a string.)
      const FMValue &operator[]( ArrayIndex index ) const;

      /// Access an array element (zero based index )
      /// (You may need to say 'FMvalue[0u]' to get your compiler to distinguish
      ///  this from the operator[] which takes a string.)
      const FMValue &operator[]( int index ) const;

      /// If the array contains at least index+1 elements, returns the element FMvalue, 
      /// otherwise returns defaultFMValue.
      FMValue get( ArrayIndex index, 
                 const FMValue &defaultFMValue ) const;
      /// Return true if index < size().
      bool isValidIndex( ArrayIndex index ) const;
      /// \brief Append FMvalue to array at the end.
      ///
      /// Equivalent to jsonFMvalue[jsonFMvalue.size()] = FMvalue;
      FMValue &append( const FMValue &FMvalue );

      /// Access an object FMvalue by name, create a null member if it does not exist.
      FMValue &operator[]( const char *key );
      /// Access an object FMvalue by name, returns null if there is no member with that name.
      const FMValue &operator[]( const char *key ) const;
      /// Access an object FMvalue by name, create a null member if it does not exist.
      FMValue &operator[]( const std::string &key );
      /// Access an object FMvalue by name, returns null if there is no member with that name.
      const FMValue &operator[]( const std::string &key ) const;
      /** \brief Access an object FMvalue by name, create a null member if it does not exist.

       * If the object as no entry for that name, then the member name used to store
       * the new entry is not duplicated.
       * Example of use:
       * \code
       * CFMJson::FMValue object;
       * static const StaticString code("code");
       * object[code] = 1234;
       * \endcode
       */
      FMValue &operator[]( const StaticString &key );
# ifdef FMJSON_USE_CPPTL
      /// Access an object FMvalue by name, create a null member if it does not exist.
      FMValue &operator[]( const CppTL::ConstString &key );
      /// Access an object FMvalue by name, returns null if there is no member with that name.
      const FMValue &operator[]( const CppTL::ConstString &key ) const;
# endif
      /// Return the member named key if it exist, defaultFMValue otherwise.
      FMValue get( const char *key, 
                 const FMValue &defaultFMValue ) const;
      /// Return the member named key if it exist, defaultFMValue otherwise.
      FMValue get( const std::string &key,
                 const FMValue &defaultFMValue ) const;
# ifdef FMJSON_USE_CPPTL
      /// Return the member named key if it exist, defaultFMValue otherwise.
      FMValue get( const CppTL::ConstString &key,
                 const FMValue &defaultFMValue ) const;
# endif
      /// \brief Remove and return the named member.  
      ///
      /// Do nothing if it did not exist.
      /// \return the removed FMValue, or null.
      /// \pre type() is objectFMValue or nullFMValue
      /// \post type() is unchanged
      FMValue removeMember( const char* key );
      /// Same as removeMember(const char*)
      FMValue removeMember( const std::string &key );

      /// Return true if the object has a member named key.
      bool isMember( const char *key ) const;
      /// Return true if the object has a member named key.
      bool isMember( const std::string &key ) const;
# ifdef FMJSON_USE_CPPTL
      /// Return true if the object has a member named key.
      bool isMember( const CppTL::ConstString &key ) const;
# endif

      /// \brief Return a list of the member names.
      ///
      /// If null, return an empty list.
      /// \pre type() is objectFMValue or nullFMValue
      /// \post if type() was nullFMValue, it remains nullFMValue
      Members getMemberNames() const;

//# ifdef FMJSON_USE_CPPTL
//      EnumMemberNames enumMemberNames() const;
//      EnumFMValues enumFMValues() const;
//# endif

      /// Comments must be //... or /* ... */
      void setComment( const char *comment,
                       CommentPlacement placement );
      /// Comments must be //... or /* ... */
      void setComment( const std::string &comment,
                       CommentPlacement placement );
      bool hasComment( CommentPlacement placement ) const;
      /// Include delimiters and embedded newlines.
      std::string getComment( CommentPlacement placement ) const;

      std::string toStyledString() const;

      const_iterator begin() const;
      const_iterator end() const;

      iterator begin();
      iterator end();

   private:
      FMValue &resolveReference( const char *key, 
                               bool isStatic );

# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
      inline bool isItemAvailable() const
      {
         return itemIsUsed_ == 0;
      }

      inline void setItemUsed( bool isUsed = true )
      {
         itemIsUsed_ = isUsed ? 1 : 0;
      }

      inline bool isMemberNameStatic() const
      {
         return memberNameIsStatic_ == 0;
      }

      inline void setMemberNameIsStatic( bool isStatic )
      {
         memberNameIsStatic_ = isStatic ? 1 : 0;
      }
# endif // # ifdef FMJSON_VALUE_USE_INTERNAL_MAP

   private:
      struct CommentInfo
      {
         CommentInfo();
         ~CommentInfo();

         void setComment( const char *text );

         char *comment_;
      };

      //struct MemberNamesTransform
      //{
      //   typedef const char *result_type;
      //   const char *operator()( const CZString &name ) const
      //   {
      //      return name.c_str();
      //   }
      //};

      union FMValueHolder
      {
         LargestInt int_;
         LargestUInt uint_;
         double real_;
         bool bool_;
         char *string_;
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
         FMValueInternalArray *array_;
         FMValueInternalMap *map_;
#else
         ObjectFMValues *map_;
# endif
      } FMvalue_;
      FMValueType type_ : 8;
      int allocated_ : 1;     // Notes: if declared as bool, bitfield is useless.
# ifdef FMJSON_VALUE_USE_INTERNAL_MAP
      unsigned int itemIsUsed_ : 1;      // used by the FMValueInternalMap container.
      int memberNameIsStatic_ : 1;       // used by the FMValueInternalMap container.
# endif
      CommentInfo *comments_;
   };


   /** \brief Experimental and untested: represents an element of the "path" to access a node.
    */
   class PathArgument
   {
   public:
      friend class Path;

      PathArgument();
      PathArgument( ArrayIndex index );
      PathArgument( const char *key );
      PathArgument( const std::string &key );

   private:
      enum Kind
      {
         kindNone = 0,
         kindIndex,
         kindKey
      };
      std::string key_;
      ArrayIndex index_;
      Kind kind_;
   };

   /** \brief Experimental and untested: represents a "path" to access a node.
    *
    * Syntax:
    * - "." => root node
    * - ".[n]" => elements at index 'n' of root node (an array FMvalue)
    * - ".name" => member named 'name' of root node (an object FMvalue)
    * - ".name1.name2.name3"
    * - ".[0][1][2].name1[3]"
    * - ".%" => member name is provided as parameter
    * - ".[%]" => index is provied as parameter
    */
   class Path
   {
   public:
      Path( const std::string &path,
            const PathArgument &a1 = PathArgument(),
            const PathArgument &a2 = PathArgument(),
            const PathArgument &a3 = PathArgument(),
            const PathArgument &a4 = PathArgument(),
            const PathArgument &a5 = PathArgument() );

      const FMValue &resolve( const FMValue &root ) const;
      FMValue resolve( const FMValue &root, 
                     const FMValue &defaultFMValue ) const;
      /// Creates the "path" to access the specified node and returns a reference on the node.
      FMValue &make( FMValue &root ) const;

   private:
      typedef std::vector<const PathArgument *> InArgs;
      typedef std::vector<PathArgument> Args;

      void makePath( const std::string &path,
                     const InArgs &in );
      void addPathInArg( const std::string &path, 
                         const InArgs &in, 
                         InArgs::const_iterator &itInArg, 
                         PathArgument::Kind kind );
      void invalidPath( const std::string &path, 
                        int location );

      Args args_;
   };



#ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   /** \brief Allocator to customize FMValue internal map.
    * Below is an example of a simple implementation (default implementation actually
    * use memory pool for speed).
    * \code
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
    * \endcode
    */ 
   class FMJSON_API FMValueMapAllocator
   {
   public:
      virtual ~FMValueMapAllocator();
      virtual FMValueInternalMap *newMap() = 0;
      virtual FMValueInternalMap *newMapCopy( const FMValueInternalMap &other ) = 0;
      virtual void destructMap( FMValueInternalMap *map ) = 0;
      virtual FMValueInternalLink *allocateMapBuckets( unsigned int size ) = 0;
      virtual void releaseMapBuckets( FMValueInternalLink *links ) = 0;
      virtual FMValueInternalLink *allocateMapLink() = 0;
      virtual void releaseMapLink( FMValueInternalLink *link ) = 0;
   };

   /** \brief FMValueInternalMap hash-map bucket chain link (for internal use only).
    * \internal previous_ & next_ allows for bidirectional traversal.
    */
   class FMJSON_API FMValueInternalLink
   {
   public:
      enum { itemPerLink = 6 };  // sizeof(FMValueInternalLink) = 128 on 32 bits architecture.
      enum InternalFlags { 
         flagAvailable = 0,
         flagUsed = 1
      };

      FMValueInternalLink();

      ~FMValueInternalLink();

      FMValue items_[itemPerLink];
      char *keys_[itemPerLink];
      FMValueInternalLink *previous_;
      FMValueInternalLink *next_;
   };


   /** \brief A linked page based hash-table implementation used internally by FMValue.
    * \internal FMValueInternalMap is a tradional bucket based hash-table, with a linked
    * list in each bucket to handle collision. There is an addional twist in that
    * each node of the collision linked list is a page containing a fixed amount of
    * FMvalue. This provides a better compromise between memory usage and speed.
    * 
    * Each bucket is made up of a chained list of FMValueInternalLink. The last
    * link of a given bucket can be found in the 'previous_' field of the following bucket.
    * The last link of the last bucket is stored in tailLink_ as it has no following bucket.
    * Only the last link of a bucket may contains 'available' item. The last link always
    * contains at least one element unless is it the bucket one very first link.
    */
   class FMJSON_API FMValueInternalMap
   {
      friend class FMValueIteratorBase;
      friend class FMValue;
   public:
      typedef unsigned int HashKey;
      typedef unsigned int BucketIndex;

# ifndef FMJSONCPP_DOC_EXCLUDE_IMPLEMENTATION
      struct IteratorState
      {
         IteratorState() 
            : map_(0)
            , link_(0)
            , itemIndex_(0)
            , bucketIndex_(0) 
         {
         }
         FMValueInternalMap *map_;
         FMValueInternalLink *link_;
         BucketIndex itemIndex_;
         BucketIndex bucketIndex_;
      };
# endif // ifndef FMJSONCPP_DOC_EXCLUDE_IMPLEMENTATION

      FMValueInternalMap();
      FMValueInternalMap( const FMValueInternalMap &other );
      FMValueInternalMap &operator =( const FMValueInternalMap &other );
      ~FMValueInternalMap();

      void swap( FMValueInternalMap &other );

      BucketIndex size() const;

      void clear();

      bool reserveDelta( BucketIndex growth );

      bool reserve( BucketIndex newItemCount );

      const FMValue *find( const char *key ) const;

      FMValue *find( const char *key );

      FMValue &resolveReference( const char *key, 
                               bool isStatic );

      void remove( const char *key );

      void doActualRemove( FMValueInternalLink *link, 
                           BucketIndex index,
                           BucketIndex bucketIndex );

      FMValueInternalLink *&getLastLinkInBucket( BucketIndex bucketIndex );

      FMValue &setNewItem( const char *key, 
                         bool isStatic, 
                         FMValueInternalLink *link, 
                         BucketIndex index );

      FMValue &unsafeAdd( const char *key, 
                        bool isStatic, 
                        HashKey hashedKey );

      HashKey hash( const char *key ) const;

      int compare( const FMValueInternalMap &other ) const;

   private:
      void makeBeginIterator( IteratorState &it ) const;
      void makeEndIterator( IteratorState &it ) const;
      static bool equals( const IteratorState &x, const IteratorState &other );
      static void increment( IteratorState &iterator );
      static void incrementBucket( IteratorState &iterator );
      static void decrement( IteratorState &iterator );
      static const char *key( const IteratorState &iterator );
      static const char *key( const IteratorState &iterator, bool &isStatic );
      static FMValue &FMvalue( const IteratorState &iterator );
      static int distance( const IteratorState &x, const IteratorState &y );

   private:
      FMValueInternalLink *buckets_;
      FMValueInternalLink *tailLink_;
      BucketIndex bucketsSize_;
      BucketIndex itemCount_;
   };

   /** \brief A simplified deque implementation used internally by FMValue.
   * \internal
   * It is based on a list of fixed "page", each page contains a fixed number of items.
   * Instead of using a linked-list, a array of pointer is used for fast item look-up.
   * Look-up for an element is as follow:
   * - compute page index: pageIndex = itemIndex / itemsPerPage
   * - look-up item in page: pages_[pageIndex][itemIndex % itemsPerPage]
   *
   * Insertion is amortized constant time (only the array containing the index of pointers
   * need to be reallocated when items are appended).
   */
   class FMJSON_API FMValueInternalArray
   {
      friend class FMValue;
      friend class FMValueIteratorBase;
   public:
      enum { itemsPerPage = 8 };    // should be a power of 2 for fast divide and modulo.
      typedef FMValue::ArrayIndex ArrayIndex;
      typedef unsigned int PageIndex;

# ifndef FMJSONCPP_DOC_EXCLUDE_IMPLEMENTATION
      struct IteratorState // Must be a POD
      {
         IteratorState() 
            : array_(0)
            , currentPageIndex_(0)
            , currentItemIndex_(0) 
         {
         }
         FMValueInternalArray *array_;
         FMValue **currentPageIndex_;
         unsigned int currentItemIndex_;
      };
# endif // ifndef FMJSONCPP_DOC_EXCLUDE_IMPLEMENTATION

      FMValueInternalArray();
      FMValueInternalArray( const FMValueInternalArray &other );
      FMValueInternalArray &operator =( const FMValueInternalArray &other );
      ~FMValueInternalArray();
      void swap( FMValueInternalArray &other );

      void clear();
      void resize( ArrayIndex newSize );

      FMValue &resolveReference( ArrayIndex index );

      FMValue *find( ArrayIndex index ) const;

      ArrayIndex size() const;

      int compare( const FMValueInternalArray &other ) const;

   private:
      static bool equals( const IteratorState &x, const IteratorState &other );
      static void increment( IteratorState &iterator );
      static void decrement( IteratorState &iterator );
      static FMValue &dereference( const IteratorState &iterator );
      static FMValue &unsafeDereference( const IteratorState &iterator );
      static int distance( const IteratorState &x, const IteratorState &y );
      static ArrayIndex indexOf( const IteratorState &iterator );
      void makeBeginIterator( IteratorState &it ) const;
      void makeEndIterator( IteratorState &it ) const;
      void makeIterator( IteratorState &it, ArrayIndex index ) const;

      void makeIndexValid( ArrayIndex index );

      FMValue **pages_;
      ArrayIndex size_;
      PageIndex pageCount_;
   };

   /** \brief Experimental: do not use. Allocator to customize FMValue internal array.
    * Below is an example of a simple implementation (actual implementation use
    * memory pool).
      \code
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

   virtual void destruct( FMValueInternalArray *array )
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
      \endcode
    */ 
   class FMJSON_API FMValueArrayAllocator
   {
   public:
      virtual ~FMValueArrayAllocator();
      virtual FMValueInternalArray *newArray() = 0;
      virtual FMValueInternalArray *newArrayCopy( const FMValueInternalArray &other ) = 0;
      virtual void destructArray( FMValueInternalArray *array ) = 0;
      /** \brief Reallocate array page index.
       * Reallocates an array of pointer on each page.
       * \param indexes [input] pointer on the current index. May be \c NULL.
       *                [output] pointer on the new index of at least 
       *                         \a minNewIndexCount pages. 
       * \param indexCount [input] current number of pages in the index.
       *                   [output] number of page the reallocated index can handle.
       *                            \b MUST be >= \a minNewIndexCount.
       * \param minNewIndexCount Minimum number of page the new index must be able to
       *                         handle.
       */
      virtual void reallocateArrayPageIndex( FMValue **&indexes, 
                                             FMValueInternalArray::PageIndex &indexCount,
                                             FMValueInternalArray::PageIndex minNewIndexCount ) = 0;
      virtual void releaseArrayPageIndex( FMValue **indexes, 
                                          FMValueInternalArray::PageIndex indexCount ) = 0;
      virtual FMValue *allocateArrayPage() = 0;
      virtual void releaseArrayPage( FMValue *FMvalue ) = 0;
   };
#endif // #ifdef FMJSON_VALUE_USE_INTERNAL_MAP


   /** \brief base class for FMValue iterators.
    *
    */
   class FMValueIteratorBase
   {
   public:
      typedef unsigned int size_t;
      typedef int difference_type;
      typedef FMValueIteratorBase SelfType;

      FMValueIteratorBase();
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
      explicit FMValueIteratorBase( const FMValue::ObjectFMValues::iterator &current );
#else
      FMValueIteratorBase( const FMValueInternalArray::IteratorState &state );
      FMValueIteratorBase( const FMValueInternalMap::IteratorState &state );
#endif

      bool operator ==( const SelfType &other ) const
      {
         return isEqual( other );
      }

      bool operator !=( const SelfType &other ) const
      {
         return !isEqual( other );
      }

      difference_type operator -( const SelfType &other ) const
      {
         return computeDistance( other );
      }

      /// Return either the index or the member name of the referenced FMvalue as a FMValue.
      FMValue key() const;

      /// Return the index of the referenced FMValue. -1 if it is not an arrayFMValue.
      UInt index() const;

      /// Return the member name of the referenced FMValue. "" if it is not an objectFMValue.
      const char *memberName() const;

   protected:
      FMValue &deref() const;

      void increment();

      void decrement();

      difference_type computeDistance( const SelfType &other ) const;

      bool isEqual( const SelfType &other ) const;

      void copy( const SelfType &other );

   private:
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
      FMValue::ObjectFMValues::iterator current_;
      // Indicates that iterator is for a null FMvalue.
      bool isNull_;
#else
      union
      {
         FMValueInternalArray::IteratorState array_;
         FMValueInternalMap::IteratorState map_;
      } iterator_;
      bool isArray_;
#endif
   };

   /** \brief const iterator for object and array FMvalue.
    *
    */
   class FMValueConstIterator : public FMValueIteratorBase
   {
      friend class FMValue;
   public:
      typedef unsigned int size_t;
      typedef int difference_type;
      typedef const FMValue &reference;
      typedef const FMValue *pointer;
      typedef FMValueConstIterator SelfType;

      FMValueConstIterator();
   private:
      /*! \internal Use by FMValue to create an iterator.
       */
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
      explicit FMValueConstIterator( const FMValue::ObjectFMValues::iterator &current );
#else
      FMValueConstIterator( const FMValueInternalArray::IteratorState &state );
      FMValueConstIterator( const FMValueInternalMap::IteratorState &state );
#endif
   public:
      SelfType &operator =( const FMValueIteratorBase &other );

      SelfType operator++( int )
      {
         SelfType temp( *this );
         ++*this;
         return temp;
      }

      SelfType operator--( int )
      {
         SelfType temp( *this );
         --*this;
         return temp;
      }

      SelfType &operator--()
      {
         decrement();
         return *this;
      }

      SelfType &operator++()
      {
         increment();
         return *this;
      }

      reference operator *() const
      {
         return deref();
      }
   };


   /** \brief Iterator for object and array FMvalue.
    */
   class FMValueIterator : public FMValueIteratorBase
   {
      friend class FMValue;
   public:
      typedef unsigned int size_t;
      typedef int difference_type;
      typedef FMValue &reference;
      typedef FMValue *pointer;
      typedef FMValueIterator SelfType;

      FMValueIterator();
      FMValueIterator( const FMValueConstIterator &other );
      FMValueIterator( const FMValueIterator &other );
   private:
      /*! \internal Use by FMValue to create an iterator.
       */
#ifndef FMJSON_VALUE_USE_INTERNAL_MAP
      explicit FMValueIterator( const FMValue::ObjectFMValues::iterator &current );
#else
      FMValueIterator( const FMValueInternalArray::IteratorState &state );
      FMValueIterator( const FMValueInternalMap::IteratorState &state );
#endif
   public:

      SelfType &operator =( const SelfType &other );

      SelfType operator++( int )
      {
         SelfType temp( *this );
         ++*this;
         return temp;
      }

      SelfType operator--( int )
      {
         SelfType temp( *this );
         --*this;
         return temp;
      }

      SelfType &operator--()
      {
         decrement();
         return *this;
      }

      SelfType &operator++()
      {
         increment();
         return *this;
      }

      reference operator *() const
      {
         return deref();
      }
   };


} // namespace CFMJson


#endif // FMCPPTL_FMJSON_H_INCLUDED
