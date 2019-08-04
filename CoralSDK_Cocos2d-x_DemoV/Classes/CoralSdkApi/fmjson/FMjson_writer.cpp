// Copyright 2007-2010 Baptiste Lepilleur
// Distributed under MIT license, or public domain if desired and
// recognized in your jurisdiction.
// See file LICENSE for detail or copy at http://jsoncpp.sourceforge.net/LICENSE

#if !defined(FMJSON_IS_AMALGAMATION)
# include "FMwriter.h"
# include "FMjson_tool.h"
#endif // if !defined(FMJSON_IS_AMALGAMATION)
#include <utility>
#include <assert.h>
#include <stdio.h>
#include <string.h>
#include <iostream>
#include <sstream>
#include <iomanip>

#if _MSC_VER >= 1400 // VC++ 8.0
#pragma warning( disable : 4996 )   // disable warning about strdup being deprecated.
#endif

namespace CFMJson {

static bool containsControlCharacter( const char* str )
{
   while ( *str ) 
   {
      if ( isControlCharacter( *(str++) ) )
         return true;
   }
   return false;
}


std::string FMvalueToString( LargestInt FMvalue )
{
   UIntToStringBuffer buffer;
   char *current = buffer + sizeof(buffer);
   bool isNegative = FMvalue < 0;
   if ( isNegative )
      FMvalue = -FMvalue;
   uintToString( LargestUInt(FMvalue), current );
   if ( isNegative )
      *--current = '-';
   assert( current >= buffer );
   return current;
}


std::string FMvalueToString( LargestUInt FMvalue )
{
   UIntToStringBuffer buffer;
   char *current = buffer + sizeof(buffer);
   uintToString( FMvalue, current );
   assert( current >= buffer );
   return current;
}

#if defined(FMJSON_HAS_INT64)

std::string FMvalueToString( Int FMvalue )
{
   return FMvalueToString( LargestInt(FMvalue) );
}


std::string FMvalueToString( UInt FMvalue )
{
   return FMvalueToString( LargestUInt(FMvalue) );
}

#endif // # if defined(FMJSON_HAS_INT64)


std::string FMvalueToString( double FMvalue )
{
   char buffer[32];
#if defined(_MSC_VER) && defined(__STDC_SECURE_LIB__) // Use secure version with visual studio 2005 to avoid warning. 
   sprintf_s(buffer, sizeof(buffer), "%#.16g", FMvalue); 
#else	
   sprintf(buffer, "%#.16g", FMvalue); 
#endif
   char* ch = buffer + strlen(buffer) - 1;
   if (*ch != '0') return buffer; // nothing to truncate, so save time
   while(ch > buffer && *ch == '0'){
     --ch;
   }
   char* last_nonzero = ch;
   while(ch >= buffer){
     switch(*ch){
     case '0':
     case '1':
     case '2':
     case '3':
     case '4':
     case '5':
     case '6':
     case '7':
     case '8':
     case '9':
       --ch;
       continue;
     case '.':
       // Truncate zeroes to save bytes in output, but keep one.
       *(last_nonzero+2) = '\0';
       return buffer;
     default:
       return buffer;
     }
   }
   return buffer;
}


std::string FMvalueToString( bool FMvalue )
{
   return FMvalue ? "true" : "false";
}

std::string FMvalueToQuotedString( const char *FMvalue )
{
   // Not sure how to handle unicode...
   if (strpbrk(FMvalue, "\"\\\b\f\n\r\t") == NULL && !containsControlCharacter( FMvalue ))
      return std::string("\"") + FMvalue + "\"";
   // We have to walk FMvalue and escape any special characters.
   // Appending to std::string is not efficient, but this should be rare.
   // (Note: forward slashes are *not* rare, but I am not escaping them.)
   std::string::size_type maxsize = strlen(FMvalue)*2 + 3; // allescaped+quotes+NULL
   std::string result;
   result.reserve(maxsize); // to avoid lots of mallocs
   result += "\"";
   for (const char* c=FMvalue; *c != 0; ++c)
   {
      switch(*c)
      {
         case '\"':
            result += "\\\"";
            break;
         case '\\':
            result += "\\\\";
            break;
         case '\b':
            result += "\\b";
            break;
         case '\f':
            result += "\\f";
            break;
         case '\n':
            result += "\\n";
            break;
         case '\r':
            result += "\\r";
            break;
         case '\t':
            result += "\\t";
            break;
         //case '/':
            // Even though \/ is considered a legal escape in JSON, a bare
            // slash is also legal, so I see no reason to escape it.
            // (I hope I am not misunderstanding something.
            // blep notes: actually escaping \/ may be useful in javascript to avoid </ 
            // sequence.
            // Should add a flag to allow this compatibility mode and prevent this 
            // sequence from occurring.
         default:
            if ( isControlCharacter( *c ) )
            {
               std::ostringstream oss;
               oss << "\\u" << std::hex << std::uppercase << std::setfill('0') << std::setw(4) << static_cast<int>(*c);
               result += oss.str();
            }
            else
            {
               result += *c;
            }
            break;
      }
   }
   result += "\"";
   return result;
}

// Class Writer
// //////////////////////////////////////////////////////////////////
Writer::~Writer()
{
}


// Class FastWriter
// //////////////////////////////////////////////////////////////////

FastWriter::FastWriter()
   : yamlCompatiblityEnabled_( false )
{
}


void 
FastWriter::enableYAMLCompatibility()
{
   yamlCompatiblityEnabled_ = true;
}


std::string 
FastWriter::write( const FMValue &root )
{
   document_ = "";
   writeFMValue( root );
   document_ += "\n";
   return document_;
}


void 
FastWriter::writeFMValue( const FMValue &FMvalue )
{
   switch ( FMvalue.type() )
   {
   case nullFMValue:
      document_ += "null";
      break;
   case intFMValue:
      document_ += FMvalueToString( FMvalue.asLargestInt() );
      break;
   case uintFMValue:
      document_ += FMvalueToString( FMvalue.asLargestUInt() );
      break;
   case realFMValue:
      document_ += FMvalueToString( FMvalue.asDouble() );
      break;
   case stringFMValue:
      document_ += FMvalueToQuotedString( FMvalue.asCString() );
      break;
   case booleanFMValue:
      document_ += FMvalueToString( FMvalue.asBool() );
      break;
   case arrayFMValue:
      {
         document_ += "[";
         int size = FMvalue.size();
         for ( int index =0; index < size; ++index )
         {
            if ( index > 0 )
               document_ += ",";
            writeFMValue( FMvalue[index] );
         }
         document_ += "]";
      }
      break;
   case objectFMValue:
      {
         FMValue::Members members( FMvalue.getMemberNames() );
         document_ += "{";
         for ( FMValue::Members::iterator it = members.begin(); 
               it != members.end(); 
               ++it )
         {
            const std::string &name = *it;
            if ( it != members.begin() )
               document_ += ",";
            document_ += FMvalueToQuotedString( name.c_str() );
            document_ += yamlCompatiblityEnabled_ ? ": " 
                                                  : ":";
            writeFMValue( FMvalue[name] );
         }
         document_ += "}";
      }
      break;
   }
}


// Class StyledWriter
// //////////////////////////////////////////////////////////////////

StyledWriter::StyledWriter()
   : rightMargin_( 74 )
   , indentSize_( 3 )
{
}


std::string 
StyledWriter::write( const FMValue &root )
{
   document_ = "";
   addChildFMValues_ = false;
   indentString_ = "";
   writeCommentBeforeFMValue( root );
   writeFMValue( root );
   writeCommentAfterFMValueOnSameLine( root );
   document_ += "\n";
   return document_;
}


void 
StyledWriter::writeFMValue( const FMValue &FMvalue )
{
   switch ( FMvalue.type() )
   {
   case nullFMValue:
      pushFMValue( "null" );
      break;
   case intFMValue:
      pushFMValue( FMvalueToString( FMvalue.asLargestInt() ) );
      break;
   case uintFMValue:
      pushFMValue( FMvalueToString( FMvalue.asLargestUInt() ) );
      break;
   case realFMValue:
      pushFMValue( FMvalueToString( FMvalue.asDouble() ) );
      break;
   case stringFMValue:
      pushFMValue( FMvalueToQuotedString( FMvalue.asCString() ) );
      break;
   case booleanFMValue:
      pushFMValue( FMvalueToString( FMvalue.asBool() ) );
      break;
   case arrayFMValue:
      writeArrayFMValue( FMvalue);
      break;
   case objectFMValue:
      {
         FMValue::Members members( FMvalue.getMemberNames() );
         if ( members.empty() )
            pushFMValue( "{}" );
         else
         {
            writeWithIndent( "{" );
            indent();
            FMValue::Members::iterator it = members.begin();
            for (;;)
            {
               const std::string &name = *it;
               const FMValue &childFMValue = FMvalue[name];
               writeCommentBeforeFMValue( childFMValue );
               writeWithIndent( FMvalueToQuotedString( name.c_str() ) );
               document_ += " : ";
               writeFMValue( childFMValue );
               if ( ++it == members.end() )
               {
                  writeCommentAfterFMValueOnSameLine( childFMValue );
                  break;
               }
               document_ += ",";
               writeCommentAfterFMValueOnSameLine( childFMValue );
            }
            unindent();
            writeWithIndent( "}" );
         }
      }
      break;
   }
}


void 
StyledWriter::writeArrayFMValue( const FMValue &FMvalue )
{
   unsigned size = FMvalue.size();
   if ( size == 0 )
      pushFMValue( "[]" );
   else
   {
      bool isArrayMultiLine = isMultineArray( FMvalue );
      if ( isArrayMultiLine )
      {
         writeWithIndent( "[" );
         indent();
         bool hasChildFMValue = !childFMValues_.empty();
         unsigned index =0;
         for (;;)
         {
            const FMValue &childFMValue = FMvalue[index];
            writeCommentBeforeFMValue( childFMValue );
            if ( hasChildFMValue )
               writeWithIndent( childFMValues_[index] );
            else
            {
               writeIndent();
               writeFMValue( childFMValue );
            }
            if ( ++index == size )
            {
               writeCommentAfterFMValueOnSameLine( childFMValue );
               break;
            }
            document_ += ",";
            writeCommentAfterFMValueOnSameLine( childFMValue );
         }
         unindent();
         writeWithIndent( "]" );
      }
      else // output on a single line
      {
         assert( childFMValues_.size() == size );
         document_ += "[ ";
         for ( unsigned index =0; index < size; ++index )
         {
            if ( index > 0 )
               document_ += ", ";
            document_ += childFMValues_[index];
         }
         document_ += " ]";
      }
   }
}


bool 
StyledWriter::isMultineArray( const FMValue &FMvalue )
{
   int size = FMvalue.size();
   bool isMultiLine = size*3 >= rightMargin_ ;
   childFMValues_.clear();
   for ( int index =0; index < size  &&  !isMultiLine; ++index )
   {
      const FMValue &childFMValue = FMvalue[index];
      isMultiLine = isMultiLine  ||
                     ( (childFMValue.isArray()  ||  childFMValue.isObject())  &&  
                        childFMValue.size() > 0 );
   }
   if ( !isMultiLine ) // check if line length > max line length
   {
      childFMValues_.reserve( size );
      addChildFMValues_ = true;
      int lineLength = 4 + (size-1)*2; // '[ ' + ', '*n + ' ]'
      for ( int index =0; index < size  &&  !isMultiLine; ++index )
      {
         writeFMValue( FMvalue[index] );
         lineLength += int( childFMValues_[index].length() );
         isMultiLine = isMultiLine  &&  hasCommentForFMValue( FMvalue[index] );
      }
      addChildFMValues_ = false;
      isMultiLine = isMultiLine  ||  lineLength >= rightMargin_;
   }
   return isMultiLine;
}


void 
StyledWriter::pushFMValue( const std::string &FMvalue )
{
   if ( addChildFMValues_ )
      childFMValues_.push_back( FMvalue );
   else
      document_ += FMvalue;
}


void 
StyledWriter::writeIndent()
{
   if ( !document_.empty() )
   {
      char last = document_[document_.length()-1];
      if ( last == ' ' )     // already indented
         return;
      if ( last != '\n' )    // Comments may add new-line
         document_ += '\n';
   }
   document_ += indentString_;
}


void 
StyledWriter::writeWithIndent( const std::string &FMvalue )
{
   writeIndent();
   document_ += FMvalue;
}


void 
StyledWriter::indent()
{
   indentString_ += std::string( indentSize_, ' ' );
}


void 
StyledWriter::unindent()
{
   assert( int(indentString_.size()) >= indentSize_ );
   indentString_.resize( indentString_.size() - indentSize_ );
}


void 
StyledWriter::writeCommentBeforeFMValue( const FMValue &root )
{
   if ( !root.hasComment( commentBefore ) )
      return;
   document_ += normalizeEOL( root.getComment( commentBefore ) );
   document_ += "\n";
}


void 
StyledWriter::writeCommentAfterFMValueOnSameLine( const FMValue &root )
{
   if ( root.hasComment( commentAfterOnSameLine ) )
      document_ += " " + normalizeEOL( root.getComment( commentAfterOnSameLine ) );

   if ( root.hasComment( commentAfter ) )
   {
      document_ += "\n";
      document_ += normalizeEOL( root.getComment( commentAfter ) );
      document_ += "\n";
   }
}


bool 
StyledWriter::hasCommentForFMValue( const FMValue &FMvalue )
{
   return FMvalue.hasComment( commentBefore )
          ||  FMvalue.hasComment( commentAfterOnSameLine )
          ||  FMvalue.hasComment( commentAfter );
}


std::string 
StyledWriter::normalizeEOL( const std::string &text )
{
   std::string normalized;
   normalized.reserve( text.length() );
   const char *begin = text.c_str();
   const char *end = begin + text.length();
   const char *current = begin;
   while ( current != end )
   {
      char c = *current++;
      if ( c == '\r' ) // mac or dos EOL
      {
         if ( *current == '\n' ) // convert dos EOL
            ++current;
         normalized += '\n';
      }
      else // handle unix EOL & other char
         normalized += c;
   }
   return normalized;
}


// Class StyledStreamWriter
// //////////////////////////////////////////////////////////////////

StyledStreamWriter::StyledStreamWriter( std::string indentation )
   : document_(NULL)
   , rightMargin_( 74 )
   , indentation_( indentation )
{
}


void
StyledStreamWriter::write( std::ostream &out, const FMValue &root )
{
   document_ = &out;
   addChildFMValues_ = false;
   indentString_ = "";
   writeCommentBeforeFMValue( root );
   writeFMValue( root );
   writeCommentAfterFMValueOnSameLine( root );
   *document_ << "\n";
   document_ = NULL; // Forget the stream, for safety.
}


void 
StyledStreamWriter::writeFMValue( const FMValue &FMvalue )
{
   switch ( FMvalue.type() )
   {
   case nullFMValue:
      pushFMValue( "null" );
      break;
   case intFMValue:
      pushFMValue( FMvalueToString( FMvalue.asLargestInt() ) );
      break;
   case uintFMValue:
      pushFMValue( FMvalueToString( FMvalue.asLargestUInt() ) );
      break;
   case realFMValue:
      pushFMValue( FMvalueToString( FMvalue.asDouble() ) );
      break;
   case stringFMValue:
      pushFMValue( FMvalueToQuotedString( FMvalue.asCString() ) );
      break;
   case booleanFMValue:
      pushFMValue( FMvalueToString( FMvalue.asBool() ) );
      break;
   case arrayFMValue:
      writeArrayFMValue( FMvalue);
      break;
   case objectFMValue:
      {
         FMValue::Members members( FMvalue.getMemberNames() );
         if ( members.empty() )
            pushFMValue( "{}" );
         else
         {
            writeWithIndent( "{" );
            indent();
            FMValue::Members::iterator it = members.begin();
            for (;;)
            {
               const std::string &name = *it;
               const FMValue &childFMValue = FMvalue[name];
               writeCommentBeforeFMValue( childFMValue );
               writeWithIndent( FMvalueToQuotedString( name.c_str() ) );
               *document_ << " : ";
               writeFMValue( childFMValue );
               if ( ++it == members.end() )
               {
                  writeCommentAfterFMValueOnSameLine( childFMValue );
                  break;
               }
               *document_ << ",";
               writeCommentAfterFMValueOnSameLine( childFMValue );
            }
            unindent();
            writeWithIndent( "}" );
         }
      }
      break;
   }
}


void 
StyledStreamWriter::writeArrayFMValue( const FMValue &FMvalue )
{
   unsigned size = FMvalue.size();
   if ( size == 0 )
      pushFMValue( "[]" );
   else
   {
      bool isArrayMultiLine = isMultineArray( FMvalue );
      if ( isArrayMultiLine )
      {
         writeWithIndent( "[" );
         indent();
         bool hasChildFMValue = !childFMValues_.empty();
         unsigned index =0;
         for (;;)
         {
            const FMValue &childFMValue = FMvalue[index];
            writeCommentBeforeFMValue( childFMValue );
            if ( hasChildFMValue )
               writeWithIndent( childFMValues_[index] );
            else
            {
               writeIndent();
               writeFMValue( childFMValue );
            }
            if ( ++index == size )
            {
               writeCommentAfterFMValueOnSameLine( childFMValue );
               break;
            }
            *document_ << ",";
            writeCommentAfterFMValueOnSameLine( childFMValue );
         }
         unindent();
         writeWithIndent( "]" );
      }
      else // output on a single line
      {
         assert( childFMValues_.size() == size );
         *document_ << "[ ";
         for ( unsigned index =0; index < size; ++index )
         {
            if ( index > 0 )
               *document_ << ", ";
            *document_ << childFMValues_[index];
         }
         *document_ << " ]";
      }
   }
}


bool 
StyledStreamWriter::isMultineArray( const FMValue &FMvalue )
{
   int size = FMvalue.size();
   bool isMultiLine = size*3 >= rightMargin_ ;
   childFMValues_.clear();
   for ( int index =0; index < size  &&  !isMultiLine; ++index )
   {
      const FMValue &childFMValue = FMvalue[index];
      isMultiLine = isMultiLine  ||
                     ( (childFMValue.isArray()  ||  childFMValue.isObject())  &&  
                        childFMValue.size() > 0 );
   }
   if ( !isMultiLine ) // check if line length > max line length
   {
      childFMValues_.reserve( size );
      addChildFMValues_ = true;
      int lineLength = 4 + (size-1)*2; // '[ ' + ', '*n + ' ]'
      for ( int index =0; index < size  &&  !isMultiLine; ++index )
      {
         writeFMValue( FMvalue[index] );
         lineLength += int( childFMValues_[index].length() );
         isMultiLine = isMultiLine  &&  hasCommentForFMValue( FMvalue[index] );
      }
      addChildFMValues_ = false;
      isMultiLine = isMultiLine  ||  lineLength >= rightMargin_;
   }
   return isMultiLine;
}


void 
StyledStreamWriter::pushFMValue( const std::string &FMvalue )
{
   if ( addChildFMValues_ )
      childFMValues_.push_back( FMvalue );
   else
      *document_ << FMvalue;
}


void 
StyledStreamWriter::writeIndent()
{
  /*
    Some comments in this method would have been nice. ;-)

   if ( !document_.empty() )
   {
      char last = document_[document_.length()-1];
      if ( last == ' ' )     // already indented
         return;
      if ( last != '\n' )    // Comments may add new-line
         *document_ << '\n';
   }
  */
   *document_ << '\n' << indentString_;
}


void 
StyledStreamWriter::writeWithIndent( const std::string &FMvalue )
{
   writeIndent();
   *document_ << FMvalue;
}


void 
StyledStreamWriter::indent()
{
   indentString_ += indentation_;
}


void 
StyledStreamWriter::unindent()
{
   assert( indentString_.size() >= indentation_.size() );
   indentString_.resize( indentString_.size() - indentation_.size() );
}


void 
StyledStreamWriter::writeCommentBeforeFMValue( const FMValue &root )
{
   if ( !root.hasComment( commentBefore ) )
      return;
   *document_ << normalizeEOL( root.getComment( commentBefore ) );
   *document_ << "\n";
}


void 
StyledStreamWriter::writeCommentAfterFMValueOnSameLine( const FMValue &root )
{
   if ( root.hasComment( commentAfterOnSameLine ) )
      *document_ << " " + normalizeEOL( root.getComment( commentAfterOnSameLine ) );

   if ( root.hasComment( commentAfter ) )
   {
      *document_ << "\n";
      *document_ << normalizeEOL( root.getComment( commentAfter ) );
      *document_ << "\n";
   }
}


bool 
StyledStreamWriter::hasCommentForFMValue( const FMValue &FMvalue )
{
   return FMvalue.hasComment( commentBefore )
          ||  FMvalue.hasComment( commentAfterOnSameLine )
          ||  FMvalue.hasComment( commentAfter );
}


std::string 
StyledStreamWriter::normalizeEOL( const std::string &text )
{
   std::string normalized;
   normalized.reserve( text.length() );
   const char *begin = text.c_str();
   const char *end = begin + text.length();
   const char *current = begin;
   while ( current != end )
   {
      char c = *current++;
      if ( c == '\r' ) // mac or dos EOL
      {
         if ( *current == '\n' ) // convert dos EOL
            ++current;
         normalized += '\n';
      }
      else // handle unix EOL & other char
         normalized += c;
   }
   return normalized;
}


std::ostream& operator<<( std::ostream &sout, const FMValue &root )
{
   CFMJson::StyledStreamWriter writer;
   writer.write(sout, root);
   return sout;
}


} // namespace CFMJson
