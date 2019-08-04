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

#include <iostream>
#include "FMContentJsonDictionary.h"

namespace FMCS {
    
    FMJSONDictionary::FMJSONDictionary()
    {
        m_cFMValue.clear();
    }
    
    
    FMJSONDictionary::~FMJSONDictionary()
    {
        m_cFMValue.clear();
    }
    
    
    void FMJSONDictionary::initWithDescription(const char *pszDescription)
    {
        CFMJson::Reader cReader;
        m_cFMValue.clear();
        if (pszDescription && *pszDescription)
        {
            std::string strFMValue = pszDescription;
            cReader.parse(strFMValue, m_cFMValue, false);
        }
    }
    
    
    void FMJSONDictionary::initWithFMValue(CFMJson::FMValue& FMvalue)
    {
        m_cFMValue = FMvalue;
    }
    
    
    void FMJSONDictionary::insertItem(const char *pszKey, int nFMValue)
    {
        m_cFMValue[pszKey] = nFMValue;
    }
    
    
    void FMJSONDictionary::insertItem(const char *pszKey, double fFMValue)
    {
        m_cFMValue[pszKey] = fFMValue;
    }
    
    
    void FMJSONDictionary::insertItem(const char *pszKey, const char * pszFMValue)
    {
        m_cFMValue[pszKey] = pszFMValue;
    }
    
    void FMJSONDictionary::insertItem(const char *pszKey, bool bFMValue)
    {
        m_cFMValue[pszKey] = bFMValue;
    }
    
    void FMJSONDictionary::insertItem(const char *pszKey, FMJSONDictionary * subDictionary)
    {
        if (subDictionary)
            m_cFMValue[pszKey] = subDictionary->m_cFMValue;
    }
    
    
    bool FMJSONDictionary::deleteItem(const char *pszKey)
    {
        if(!m_cFMValue.isMember(pszKey))
            return false;
        
        m_cFMValue.removeMember(pszKey);
        
        return true;
    }
    
    
    void FMJSONDictionary::cleanUp()
    {
        m_cFMValue.clear();
    }
    
    
    bool FMJSONDictionary::isKeyValidate(const char *pszKey)
    {
        return m_cFMValue.isMember(pszKey);
    }
    
    
    int FMJSONDictionary::getItemIntFMValue(const char *pszKey, int nDefaultFMValue)
    {
        if (!isKeyValidate(pszKey, m_cFMValue) || !m_cFMValue[pszKey].isNumeric())
            return nDefaultFMValue;
        
        return m_cFMValue[pszKey].asInt();
    }
    
    
    double FMJSONDictionary::getItemFloatFMValue(const char *pszKey, double fDefaultFMValue)
    {
        if (!isKeyValidate(pszKey, m_cFMValue) || !m_cFMValue[pszKey].isNumeric())
            return fDefaultFMValue;
        
        return m_cFMValue[pszKey].asDouble();
    }
    
    
    const char * FMJSONDictionary::getItemStringFMValue(const char *pszKey)
    {
        if (!isKeyValidate(pszKey, m_cFMValue) || !m_cFMValue[pszKey].isString())
            return NULL;
        
        return m_cFMValue[pszKey].asCString();
    }
    
    bool FMJSONDictionary::getItemBoolFMvalue(const char *pszKey, bool bDefaultFMValue)
    {
        if (!isKeyValidate(pszKey, m_cFMValue) || !m_cFMValue[pszKey].isBool())
            return bDefaultFMValue;
        
        return m_cFMValue[pszKey].asBool();
    }
    
    
    FMJSONDictionary * FMJSONDictionary::getSubDictionary(const char *pszKey)
    {
        FMJSONDictionary * pNewDictionary;
        if (!isKeyValidate(pszKey, m_cFMValue) || (!m_cFMValue[pszKey].isArray() &&
                                                 !m_cFMValue[pszKey].isObject() &&
                                                 !m_cFMValue[pszKey].isConvertibleTo(CFMJson::arrayFMValue) &&
                                                 !m_cFMValue[pszKey].isConvertibleTo(CFMJson::objectFMValue)))
        {
            pNewDictionary = NULL;
        }
        else
        {
            pNewDictionary = new FMJSONDictionary();
            pNewDictionary->initWithFMValue(m_cFMValue[pszKey]);
        }
        return pNewDictionary;
    }
    
    
    std::string FMJSONDictionary::getDescription()
    {
        std::string strReturn = m_cFMValue.toStyledString();
        return strReturn;
    }
    
    
    bool FMJSONDictionary::insertItemToArray(const char *pszArrayKey, int nFMValue)
    {
        CFMJson::FMValue array;
        if(m_cFMValue.isMember(pszArrayKey))
        {
            if (!m_cFMValue[pszArrayKey].isArray() && !m_cFMValue[pszArrayKey].isConvertibleTo(CFMJson::arrayFMValue))
                return false;
            
            array = m_cFMValue[pszArrayKey];
        }
        
        array.append(nFMValue);
        m_cFMValue[pszArrayKey] = array;
        
        return true;
    }
    
    
    bool FMJSONDictionary::insertItemToArray(const char *pszArrayKey, double fFMValue)
    {
        CFMJson::FMValue array;
        if(m_cFMValue.isMember(pszArrayKey))
        {
            if (!m_cFMValue[pszArrayKey].isArray() && !m_cFMValue[pszArrayKey].isConvertibleTo(CFMJson::arrayFMValue))
                return false;
            
            array = m_cFMValue[pszArrayKey];
        }
        
        array.append(fFMValue);
        m_cFMValue[pszArrayKey] = array;
        
        return true;
    }
    
    
    bool FMJSONDictionary::insertItemToArray(const char *pszArrayKey, const char * pszFMValue)
    {
        CFMJson::FMValue array;
        if(m_cFMValue.isMember(pszArrayKey))
        {
            if (!m_cFMValue[pszArrayKey].isArray() && !m_cFMValue[pszArrayKey].isConvertibleTo(CFMJson::arrayFMValue))
                return false;
            
            array = m_cFMValue[pszArrayKey];
        }
        
        array.append(pszFMValue);
        m_cFMValue[pszArrayKey] = array;
        
        return true;
    }
    
    
    bool FMJSONDictionary::insertItemToArray(const char *pszArrayKey, FMJSONDictionary * subDictionary)
    {
        CFMJson::FMValue array;
        if(m_cFMValue.isMember(pszArrayKey))
        {
            if (!m_cFMValue[pszArrayKey].isArray() && !m_cFMValue[pszArrayKey].isConvertibleTo(CFMJson::arrayFMValue))
                return false;
            
            array = m_cFMValue[pszArrayKey];
        }
        
        array.append(subDictionary->m_cFMValue);
        m_cFMValue[pszArrayKey] = array;
        
        return true;
    }
    
    
    int FMJSONDictionary::getItemCount()
    {
        return m_cFMValue.size();
    }
    
    
    DicItemType FMJSONDictionary::getItemType(int nIndex)
    {
        return (DicItemType)m_cFMValue[nIndex].type();
    }
    
    
    DicItemType FMJSONDictionary::getItemType(const char *pszKey)
    {
        return (DicItemType)m_cFMValue[pszKey].type();
    }
    
    std::vector<std::string> FMJSONDictionary::getAllMemberNames()
    {
        return m_cFMValue.getMemberNames();
    }
    
    
    int FMJSONDictionary::getArrayItemCount(const char *pszArrayKey)
    {
        int nRet = 0;
        if (!isKeyValidate(pszArrayKey, m_cFMValue) ||
            (!m_cFMValue[pszArrayKey].isArray() && !m_cFMValue[pszArrayKey].isObject() &&
             !m_cFMValue[pszArrayKey].isConvertibleTo(CFMJson::arrayFMValue) && !m_cFMValue[pszArrayKey].isConvertibleTo(CFMJson::objectFMValue)))
        {
            nRet = 0;
        }
        else
        {
            CFMJson::FMValue arrayFMValue = m_cFMValue[pszArrayKey];
            nRet = arrayFMValue.size();
        }
        
        return nRet;
    }
    
    
    int FMJSONDictionary::getIntFMValueFromArray(const char *pszArrayKey, int nIndex, int nDefaultFMValue)
    {
        int nRet = nDefaultFMValue;
        CFMJson::FMValue * arrayFMValue = validateArrayItem(pszArrayKey, nIndex);
        if (arrayFMValue)
        {
            if ((*arrayFMValue)[nIndex].isNumeric())
                nRet = (*arrayFMValue)[nIndex].asInt();
        }
        
        return nRet;
    }
    
    
    double FMJSONDictionary::getFloatFMValueFromArray(const char *pszArrayKey, int nIndex, double fDefaultFMValue)
    {
        double fRet = fDefaultFMValue;
        CFMJson::FMValue * arrayFMValue = validateArrayItem(pszArrayKey, nIndex);
        if (arrayFMValue)
        {
            if ((*arrayFMValue)[nIndex].isNumeric())
                fRet = (*arrayFMValue)[nIndex].asDouble();
        }
        
        return fRet;
    }
    
    bool FMJSONDictionary::getBoolFMValueFromArray(const char *pszArrayKey, int nIndex, bool bDefaultFMValue)
    {
        bool bRet = bDefaultFMValue;
        CFMJson::FMValue * arrayFMValue = validateArrayItem(pszArrayKey, nIndex);
        if (arrayFMValue)
        {
            if ((*arrayFMValue)[nIndex].isNumeric())
                bRet = (*arrayFMValue)[nIndex].asBool();
        }
        
        return bRet;
    }
    
    
    const char * FMJSONDictionary::getStringFMValueFromArray(const char *pszArrayKey, int nIndex)
    {
        CFMJson::FMValue * arrayFMValue = validateArrayItem(pszArrayKey, nIndex);
        if (arrayFMValue)
        {
            if ((*arrayFMValue)[nIndex].isString())
                return (*arrayFMValue)[nIndex].asCString();
        }
        
        return NULL;
    }
    
    
    FMJSONDictionary * FMJSONDictionary::getSubItemFromArray(const char *pszArrayKey, int nIndex)
    {
        CFMJson::FMValue * arrayFMValue = validateArrayItem(pszArrayKey, nIndex);
        if (arrayFMValue)
        {
            if ((*arrayFMValue)[nIndex].isArray() || (*arrayFMValue)[nIndex].isObject())
            {
                FMJSONDictionary * pNewDictionary = new FMJSONDictionary();
                pNewDictionary->initWithFMValue((*arrayFMValue)[nIndex]);
                return pNewDictionary;
            }
        }
        
        return NULL;
    }
    
    
    DicItemType FMJSONDictionary::getItemTypeFromArray(const char *pszArrayKey, int nIndex)
    {
        CFMJson::FMValue * arrayFMValue = validateArrayItem(pszArrayKey, nIndex);
        if (arrayFMValue)
            return (DicItemType)((*arrayFMValue)[nIndex].type());
        
        return (DicItemType)CFMJson::nullFMValue;
    }
    
    
    inline bool FMJSONDictionary::isKeyValidate(const char *pszKey, CFMJson::FMValue& root)
    {
        if (root.isNull() || !root.isMember(pszKey))
            return false;
        
        return true;
    }
    
    
    inline CFMJson::FMValue * FMJSONDictionary::validateArrayItem(const char *pszArrayKey, int nIndex)
    {
        if (!isKeyValidate(pszArrayKey, m_cFMValue) && !m_cFMValue[pszArrayKey].isArray() && !m_cFMValue[pszArrayKey].isConvertibleTo(CFMJson::arrayFMValue))
            return NULL;
        if (!m_cFMValue[pszArrayKey].isValidIndex(nIndex))
            return NULL;
        
        return &m_cFMValue[pszArrayKey];
    }
}
