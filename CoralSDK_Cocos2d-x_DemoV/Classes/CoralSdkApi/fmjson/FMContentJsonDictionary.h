/*
 * Copyright (c) 2012 Chukong Technologies, Inc.
 *
 * http://www.cocostudio.com
 * http://tools.cocoachina.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

#ifndef COCOSTUDIO_FMCONTENTJSONDICTIONARY_H
#define COCOSTUDIO_FMCONTENTJSONDICTIONARY_H


#include "FMjson_lib.h"
#include <vector>
#include <string>

namespace FMCS {

    typedef enum _DicItemType
    {
        EDIC_TYPENULL = 0,
        EDIC_TYPEINT,
        EDIC_TYPEUINT,
        EDIC_TYPEFLOAT,
        EDIC_TYPESTRING,
        EDIC_TYPEBOOLEN,
        EDIC_TYPEARRAY,
        EDIC_TYPEOBJECT
    }DicItemType;

    class FMJSONDictionary
    {
    public:
        FMJSONDictionary();
        ~FMJSONDictionary();

    public:
        void    initWithDescription(const char *pszDescription);
        void    insertItem(const char *pszKey, int nFMValue);
        void    insertItem(const char *pszKey, double fFMValue);
        void    insertItem(const char *pszKey, const char * pszFMValue);
        void    insertItem(const char *pszKey, FMJSONDictionary * subDictionary);
        void    insertItem(const char *pszKey, bool bFMValue);
        bool    deleteItem(const char *pszKey);
        void    cleanUp();
        bool    isKeyValidate(const char *pszKey);

        int             getItemIntFMValue(const char *pszKey, int nDefaultFMValue);
        double          getItemFloatFMValue(const char *pszKey, double fDefaultFMValue);
        const char *    getItemStringFMValue(const char *pszKey);
        bool            getItemBoolFMvalue(const char *pszKey, bool bDefaultFMValue);
        FMJSONDictionary *   getSubDictionary(const char *pszKey);

        std::string          getDescription();

        bool    insertItemToArray(const char *pszArrayKey, int nFMValue);
        bool    insertItemToArray(const char *pszArrayKey, double fFMValue);
        bool    insertItemToArray(const char *pszArrayKey, const char * pszFMValue);
        bool    insertItemToArray(const char *pszArrayKey, FMJSONDictionary * subDictionary);

        int getArrayItemCount(const char *pszArrayKey);
        int getIntFMValueFromArray(const char *pszArrayKey, int nIndex, int nDefaultFMValue);
        double getFloatFMValueFromArray(const char *pszArrayKey, int nIndex, double fDefaultFMValue);
        bool getBoolFMValueFromArray(const char *pszArrayKey, int nIndex, bool bDefaultFMValue);
        const char * getStringFMValueFromArray(const char *pszArrayKey, int nIndex);
        FMJSONDictionary *getSubItemFromArray(const char *pszArrayKey, int nIndex);
        DicItemType getItemTypeFromArray(const char *pszArrayKey, int nIndex);

        int         getItemCount();
        DicItemType getItemType(int nIndex);
        DicItemType getItemType(const char *pszKey);
        std::vector<std::string> getAllMemberNames();

    protected:
        CFMJson::FMValue m_cFMValue;

    private:
        void initWithFMValue(CFMJson::FMValue& FMvalue);
        inline bool isKeyValidate(const char *pszKey, CFMJson::FMValue& root);
        inline CFMJson::FMValue * validateArrayItem(const char *pszArrayKey, int nIndex);
    };

}

#endif
