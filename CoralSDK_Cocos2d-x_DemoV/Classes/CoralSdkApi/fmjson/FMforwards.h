// Copyright 2007-2010 Baptiste Lepilleur
// Distributed under MIT license, or public domain if desired and
// recognized in your jurisdiction.
// See file LICENSE for detail or copy at http://jsoncpp.sourceforge.net/LICENSE

#ifndef FMJSON_FORWARDS_H_INCLUDED
# define FMJSON_FORWARDS_H_INCLUDED

#if !defined(FMJSON_IS_AMALGAMATION)
# include "FMconfig.h"
#endif // if !defined(FMJSON_IS_AMALGAMATION)

namespace CFMJson {

   // writer.h
   class FastWriter;
   class StyledWriter;

   // reader.h
   class Reader;

   // features.h
   class Features;

   // FMvalue.h
   typedef unsigned int ArrayIndex;
   class StaticString;
   class Path;
   class PathArgument;
   class FMValue;
   class FMValueIteratorBase;
   class FMValueIterator;
   class FMValueConstIterator;
#ifdef FMJSON_VALUE_USE_INTERNAL_MAP
   class FMValueMapAllocator;
   class FMValueInternalLink;
   class FMValueInternalArray;
   class FMValueInternalMap;
#endif // #ifdef FMJSON_VALUE_USE_INTERNAL_MAP

} // namespace CFMJson


#endif // FMJSON_FORWARDS_H_INCLUDED
