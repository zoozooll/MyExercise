// Copyright 2007-2010 Baptiste Lepilleur
// Distributed under MIT license, or public domain if desired and
// recognized in your jurisdiction.
// See file LICENSE for detail or copy at http://jsoncpp.sourceforge.net/LICENSE

#ifndef FMJSON_CONFIG_H_INCLUDED
# define FMJSON_CONFIG_H_INCLUDED

/// If defined, indicates that json library is embedded in CppTL library.
//# define FMJSON_IN_CPPTL 1

/// If defined, indicates that json may leverage CppTL library
//#  define FMJSON_USE_CPPTL 1
/// If defined, indicates that cpptl vector based map should be used instead of std::map
/// as Value container.
//#  define FMJSON_USE_FMCPPTL_SMALLMAP 1
/// If defined, indicates that Json specific container should be used
/// (hash table & simple deque container with customizable allocator).
/// THIS FEATURE IS STILL EXPERIMENTAL! There is know bugs: See #3177332
//#  define FMJSON_VALUE_USE_INTERNAL_MAP 1
/// Force usage of standard new/malloc based allocator instead of memory pool based allocator.
/// The memory pools allocator used optimization (initializing Value and ValueInternalLink
/// as if it was a POD) that may cause some validation tool to report errors.
/// Only has effects if FMJSON_VALUE_USE_INTERNAL_MAP is defined.
//#  define FMJSON_USE_SIMPLE_INTERNAL_ALLOCATOR 1

/// If defined, indicates that Json use exception to report invalid type manipulation
/// instead of C assert macro.
# define FMJSON_USE_EXCEPTION 1

/// If defined, indicates that the source file is amalgated
/// to prevent private header inclusion.
/// Remarks: it is automatically defined in the generated amalgated header.
// #define FMJSON_IS_AMALGAMATION


# ifdef FMJSON_IN_CPPTL
#  include <cpptl/config.h>
#  ifndef FMJSON_USE_CPPTL
#   define FMJSON_USE_CPPTL 1
#  endif
# endif

# ifdef FMJSON_IN_CPPTL
#  define FMJSON_API FMCPPTL_API
# elif defined(FMJSON_DLL_BUILD)
#  define FMJSON_API __declspec(dllexport)
# elif defined(FMJSON_DLL)
#  define FMJSON_API __declspec(dllimport)
# else
#  define FMJSON_API
# endif

// If FMJSON_NO_INT64 is defined, then Json only support C++ "int" type for integer
// Storages, and 64 bits integer support is disabled.
// #define FMJSON_NO_INT64 1

#if defined(_MSC_VER)  &&  _MSC_VER <= 1200 // MSVC 6
// Microsoft Visual Studio 6 only support conversion from __int64 to double
// (no conversion from unsigned __int64).
#define FMJSON_USE_INT64_DOUBLE_CONVERSION 1
#endif // if defined(_MSC_VER)  &&  _MSC_VER < 1200 // MSVC 6

#if defined(_MSC_VER)  &&  _MSC_VER >= 1500 // MSVC 2008
/// Indicates that the following function is deprecated.
# define FMJSONCPP_DEPRECATED(message) __declspec(deprecated(message))
#endif

#if !defined(FMJSONCPP_DEPRECATED)
# define FMJSONCPP_DEPRECATED(message)
#endif // if !defined(FMJSONCPP_DEPRECATED)

namespace CFMJson {
   typedef int Int;
   typedef unsigned int UInt;
# if defined(FMJSON_NO_INT64)
   typedef int LargestInt;
   typedef unsigned int LargestUInt;
#  undef FMJSON_HAS_INT64
# else // if defined(FMJSON_NO_INT64)
   // For Microsoft Visual use specific types as long long is not supported
#  if defined(_MSC_VER) // Microsoft Visual Studio
   typedef __int64 Int64;
   typedef unsigned __int64 UInt64;
#  else // if defined(_MSC_VER) // Other platforms, use long long
   typedef long long int Int64;
   typedef unsigned long long int UInt64;
#  endif // if defined(_MSC_VER)
   typedef Int64 LargestInt;
   typedef UInt64 LargestUInt;
#  define FMJSON_HAS_INT64
# endif // if defined(FMJSON_NO_INT64)
} // end namespace CFMJson


#endif // FMJSON_CONFIG_H_INCLUDED
