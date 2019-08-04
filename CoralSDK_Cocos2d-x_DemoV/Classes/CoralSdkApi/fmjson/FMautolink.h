// Copyright 2007-2010 Baptiste Lepilleur
// Distributed under MIT license, or public domain if desired and
// recognized in your jurisdiction.
// See file LICENSE for detail or copy at http://jsoncpp.sourceforge.net/LICENSE

#ifndef FMFMJSON_AUTOLINK_H_INCLUDED
# define FMFMJSON_AUTOLINK_H_INCLUDED

# include "FMconfig.h"

# ifdef FMFMJSON_IN_CPPTL
#  include <cpptl/cpptl_autolink.h>
# endif

# if !defined(FMFMJSON_NO_AUTOLINK)  &&  !defined(FMJSON_DLL_BUILD)  &&  !defined(FMJSON_IN_CPPTL)
#  define FMCPPTL_AUTOLINK_NAME "json"
#  undef FMCPPTL_AUTOLINK_DLL
#  ifdef FMJSON_DLL
#   define FMCPPTL_AUTOLINK_DLL
#  endif
#  include "FMautolink.h"
# endif

#endif // FMJSON_AUTOLINK_H_INCLUDED
