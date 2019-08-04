// Copyright 2007-2010 Baptiste Lepilleur
// Distributed under MIT license, or public domain if desired and
// recognized in your jurisdiction.
// See file LICENSE for detail or copy at http://jsoncpp.sourceforge.net/LICENSE

#ifndef FMJSON_WRITER_H_INCLUDED
# define FMJSON_WRITER_H_INCLUDED

#if !defined(FMJSON_IS_AMALGAMATION)
# include "FMvalue.h"
#endif // if !defined(FMJSON_IS_AMALGAMATION)
# include <vector>
# include <string>
# include <iostream>

namespace CFMJson {

   class FMValue;

   /** \brief Abstract class for writers.
    */
   class FMJSON_API Writer
   {
   public:
      virtual ~Writer();

      virtual std::string write( const FMValue &root ) = 0;
   };

   /** \brief Outputs a FMValue in <a HREF="http://www.json.org">JSON</a> format without formatting (not human friendly).
    *
    * The JSON document is written in a single line. It is not intended for 'human' consumption,
    * but may be usefull to support feature such as RPC where bandwith is limited.
    * \sa Reader, FMValue
    */
   class FMJSON_API FastWriter : public Writer
   {
   public:
      FastWriter();
      virtual ~FastWriter(){}

      void enableYAMLCompatibility();

   public: // overridden from Writer
      virtual std::string write( const FMValue &root );

   private:
      void writeFMValue( const FMValue &FMvalue );

      std::string document_;
      bool yamlCompatiblityEnabled_;
   };

   /** \brief Writes a FMValue in <a HREF="http://www.json.org">JSON</a> format in a human friendly way.
    *
    * The rules for line break and indent are as follow:
    * - Object FMvalue:
    *     - if empty then print {} without indent and line break
    *     - if not empty the print '{', line break & indent, print one FMvalue per line
    *       and then unindent and line break and print '}'.
    * - Array FMvalue:
    *     - if empty then print [] without indent and line break
    *     - if the array contains no object FMvalue, empty array or some other FMvalue types,
    *       and all the FMvalues fit on one lines, then print the array on a single line.
    *     - otherwise, it the FMvalues do not fit on one line, or the array contains
    *       object or non empty array, then print one FMvalue per line.
    *
    * If the FMValue have comments then they are outputed according to their #CommentPlacement.
    *
    * \sa Reader, FMValue, FMValue::setComment()
    */
   class FMJSON_API StyledWriter: public Writer
   {
   public:
      StyledWriter();
      virtual ~StyledWriter(){}

   public: // overridden from Writer
      /** \brief Serialize a FMValue in <a HREF="http://www.json.org">JSON</a> format.
       * \param root FMValue to serialize.
       * \return String containing the JSON document that represents the root FMvalue.
       */
      virtual std::string write( const FMValue &root );

   private:
      void writeFMValue( const FMValue &FMvalue );
      void writeArrayFMValue( const FMValue &FMvalue );
      bool isMultineArray( const FMValue &FMvalue );
      void pushFMValue( const std::string &FMvalue );
      void writeIndent();
      void writeWithIndent( const std::string &FMvalue );
      void indent();
      void unindent();
      void writeCommentBeforeFMValue( const FMValue &root );
      void writeCommentAfterFMValueOnSameLine( const FMValue &root );
      bool hasCommentForFMValue( const FMValue &FMvalue );
      static std::string normalizeEOL( const std::string &text );

      typedef std::vector<std::string> ChildFMValues;

      ChildFMValues childFMValues_;
      std::string document_;
      std::string indentString_;
      int rightMargin_;
      int indentSize_;
      bool addChildFMValues_;
   };

   /** \brief Writes a FMValue in <a HREF="http://www.json.org">JSON</a> format in a human friendly way,
        to a stream rather than to a string.
    *
    * The rules for line break and indent are as follow:
    * - Object FMvalue:
    *     - if empty then print {} without indent and line break
    *     - if not empty the print '{', line break & indent, print one FMvalue per line
    *       and then unindent and line break and print '}'.
    * - Array FMvalue:
    *     - if empty then print [] without indent and line break
    *     - if the array contains no object FMvalue, empty array or some other FMvalue types,
    *       and all the FMvalues fit on one lines, then print the array on a single line.
    *     - otherwise, it the FMvalues do not fit on one line, or the array contains
    *       object or non empty array, then print one FMvalue per line.
    *
    * If the FMValue have comments then they are outputed according to their #CommentPlacement.
    *
    * \param indentation Each level will be indented by this amount extra.
    * \sa Reader, FMValue, FMValue::setComment()
    */
   class FMJSON_API StyledStreamWriter
   {
   public:
      StyledStreamWriter( std::string indentation="\t" );
      ~StyledStreamWriter(){}

   public:
      /** \brief Serialize a FMValue in <a HREF="http://www.json.org">JSON</a> format.
       * \param out Stream to write to. (Can be ostringstream, e.g.)
       * \param root FMValue to serialize.
       * \note There is no point in deriving from Writer, since write() should not return a FMvalue.
       */
      void write( std::ostream &out, const FMValue &root );

   private:
      void writeFMValue( const FMValue &FMvalue );
      void writeArrayFMValue( const FMValue &FMvalue );
      bool isMultineArray( const FMValue &FMvalue );
      void pushFMValue( const std::string &FMvalue );
      void writeIndent();
      void writeWithIndent( const std::string &FMvalue );
      void indent();
      void unindent();
      void writeCommentBeforeFMValue( const FMValue &root );
      void writeCommentAfterFMValueOnSameLine( const FMValue &root );
      bool hasCommentForFMValue( const FMValue &FMvalue );
      static std::string normalizeEOL( const std::string &text );

      typedef std::vector<std::string> ChildFMValues;

      ChildFMValues childFMValues_;
      std::ostream* document_;
      std::string indentString_;
      int rightMargin_;
      std::string indentation_;
      bool addChildFMValues_;
   };

# if defined(FMJSON_HAS_INT64)
   std::string FMJSON_API FMvalueToString( Int FMvalue );
   std::string FMJSON_API FMvalueToString( UInt FMvalue );
# endif // if defined(FMJSON_HAS_INT64)
   std::string FMJSON_API FMvalueToString( LargestInt FMvalue );
   std::string FMJSON_API FMvalueToString( LargestUInt FMvalue );
   std::string FMJSON_API FMvalueToString( double FMvalue );
   std::string FMJSON_API FMvalueToString( bool FMvalue );
   std::string FMJSON_API FMvalueToQuotedString( const char *FMvalue );

   /// \brief Output using the StyledStreamWriter.
   /// \see CFMJson::operator>>()
   std::ostream& operator<<( std::ostream&, const FMValue &root );

} // namespace CFMJson



#endif // FMJSON_WRITER_H_INCLUDED
