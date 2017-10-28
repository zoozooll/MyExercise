/**
 * modified by badqiu (badqiu@gmail.com)
 * web site: http://wiki.javascud.org/display/si/Javascript_RapidValidation
 * bug report: http://jira.javascud.org/browse/SI
 */

/*
 * Really easy field validation with Prototype
 * http://tetlaw.id.au/view/blog/really-easy-field-validation-with-prototype
 * Andrew Tetlaw
 * Version 1.5.3 (2006-07-15)
 * 
 * Copyright (c) 2006 Andrew Tetlaw
 * http://www.opensource.org/licenses/mit-license.php
 */
Validator = Class.create();

Validator.messagesSourceEn = [
	['validation-failed' , 'Validation failed.'],
	['required' , 'This is a required field.'],
	['validate-number' , 'Please enter a valid number in this field.'],
	['validate-digits' , 'Please use numbers only in this field. please avoid spaces or other characters such as dots or commas.'],
	['validate-alpha' , 'Please use letters only (a-z) in this field.'],
	['validate-alphanum' , 'Please use only letters (a-z) or numbers (0-9) only in this field. No spaces or other characters are allowed.'],
	['validate-email' , 'Please enter a valid email address. For example fred@domain.com .'],
	['validate-url' , 'Please enter a valid URL.'],
	['validate-currency-dollar' , 'Please enter a valid $ amount. For example $100.00 .'],
	['validate-one-required' , 'Please select one of the above options.'],
	['validate-integer' , 'Please enter a valid integer in this field'],
	['validate-pattern' , 'Validation failed.'],
	['validate-ip','Please enter a valid IP address'],
	['min-value' , 'min value is %s.'],
	['max-value' , 'max value is %s.'],
	['min-length' , 'min length is %s,current length is %s.'],
	['max-length' , 'max length is %s,current length is %s.'],
	['int-range' , 'Please enter integer value between %s and %s'],
	['float-range' , 'Please enter number between %s and %s'],
	['length-range' , 'Please enter value length between %s and %s,current length is %s'],
	['equals','Conflicting with above value.'],
	['less-than','Input value must be less than above value.'],
	['great-than','Input value must be great than above value.'],
	['validate-date' , 'Please use this date format: %s. For example %s.'],
	['validate-file' , function(v,elm,args,metadata) {
		return ValidationUtils.format("Please enter file type in [%s]",[args.join(',')]);
	}],
	//�й����е������֤��ʾ��Ϣ
	['validate-id-number','Please enter a valid id number.'],
	['validate-chinese','Please enter chinese'],
	['validate-phone','Please enter a valid phone number,current length is %s.'],
	['validate-mobile-phone','Please enter a valid mobile phone,For example 13910001000.current length is %s.'],
	['validate-zip','Please enter a valid zip code.'],
	['validate-qq','Please enter a valid qq number']
]

Validator.messagesSourceCn = [
	['validation-failed' , '��֤ʧ��.'],
	['required' , '����Ϊ����.'],
	['validate-number' , '��������Ч������.'],
	['validate-digits' , '����������.'],
	['validate-alpha' , '������Ӣ����ĸ.'],
	['validate-alphanum' , '������Ӣ����ĸ��������,�����ַ��ǲ������.'],
	['validate-email' , '��������Ч���ʼ���ַ,�� **@**.com.'],
	['validate-url' , '��������Ч��URL��ַ.'],
	['validate-currency-dollar' , 'Please enter a valid $ amount. For example $100.00 .'],
	['validate-one-required' , '������ѡ������ѡ��һ��.'],
	['validate-integer' , '��������ȷ������'],
	['validate-pattern' , '�����ֵ��ƥ��'],
	['validate-ip','��������ȷ��IP��ַ'],
	['min-value' , '��СֵΪ%s'],
	['max-value' , '���ֵΪ%s'],
	['min-length' , '��С����Ϊ%s,��ǰ����Ϊ%s.'],
	['max-length', '��󳤶�Ϊ%s,��ǰ����Ϊ%s.'],
	['int-range' , '����ֵӦ��Ϊ %s �� %s ������'],
	['float-range' , '����ֵӦ��Ϊ %s �� %s ������'],
	['length-range' , '����ֵ�ĳ���Ӧ���� %s �� %s ֮��,��ǰ����Ϊ%s'],
	['equals','�������벻һ��,����������'],
	['less-than','������С��ǰ���ֵ'],
	['great-than','���������ǰ���ֵ'],
	['validate-date' , '��������Ч������,��ʽΪ %s. ����:%s.'],
	['validate-file' , function(v,elm,args,metadata) {
		return ValidationUtils.format("�ļ�����Ӧ��Ϊ[%s]����֮һ",[args.join(',')]);
	}],
	//�й����е������֤��ʾ��Ϣ
	['validate-id-number','������Ϸ������֤����'],
	['validate-chinese','����������'],
	['validate-phone','��:020-88888888,��ǰ����Ϊ%s.'],
	['validate-mobile-phone','��������ȷ���ֻ�����,��ǰ����Ϊ%s.'],
	['validate-zip','��������Ч����������'],
	['validate-qq','��������Ч��QQ����.']
]

Validator.messagesSource = Validator.messagesSourceCn;
Validator.messages = {};
//init Validator.messages
Validator.messagesSource.each(function(ms){
	Validator.messages[ms[0]] = ms[1];
});


Validator.prototype = {
	initialize : function(className, test, options) {
		this.options = Object.extend({
			ignoreEmptyValue : true,
			depends : []
		}, options || {});
		this._test = test ? test : function(v,elm){ return true };
		this._error = Validator.messages[className] ? Validator.messages[className] : Validator.messages['validation-failed'];
		this.className = className;
		this._dependsTest = this._dependsTest.bind(this);
		this._getDependError = this._getDependError.bind(this);
	},
	_dependsTest : function(v,elm) {
		if(this.options.depends && this.options.depends.length > 0) {
			var dependsResult = $A(this.options.depends).all(function(depend){
				return Validation.get(depend).test(v,elm);
			});
			return dependsResult;
		}
		return true;
	},
	test : function(v, elm) {
		if(!this._dependsTest(v,elm))
			return false;
		if(!elm) elm = {}
		return (this.options.ignoreEmptyValue && ((v == null) || (v.length == 0))) || this._test(v,elm,ValidationUtils.getArgumentsByClassName(this.className,elm.className),this);
	},
	_getDependError : function(v,elm,useTitle) {
		var dependError = null;
		$A(this.options.depends).any(function(depend){
			var validation = Validation.get(depend);
			if(!validation.test(v,elm))  {
				dependError = validation.error(v,elm,useTitle)
				return true;
			}
			return false;
		});
		return dependError;
	}, 
	error : function(v,elm,useTitle) {
		var dependError = this._getDependError(v,elm,useTitle);
		if(dependError != null) return dependError;

		var args  = ValidationUtils.getArgumentsByClassName(this.className,elm.className);
		var error = this._error;
		if(typeof error == 'string') {
			if(v) args.push(v.length);
			error = ValidationUtils.format(this._error,args);
		}else if(typeof error == 'function') {
			error = error(v,elm,args,this);
		}else {
			alert('error must type of string or function');
		}
		if(!useTitle) useTitle = elm.className.indexOf('useTitle') >= 0;
		return useTitle ? ((elm && elm.title) ? elm.title : error) : error;
	}
}

var Validation = Class.create();

Validation.prototype = {
	initialize : function(form, options){
		this.options = Object.extend({
			onSubmit : true,
			stopOnFirst : false,
			immediate : false,
			focusOnError : true,
			useTitles : false,
			onFormValidate : function(result, form) {},
			onElementValidate : function(result, elm) {}
		}, options || {});
		this.form = $(form);
		var formId =  ValidationUtils.getElmID($(form));
		Validation.validations[formId] = this;
		if(this.options.onSubmit) Event.observe(this.form,'submit',this.onSubmit.bind(this),false);
		if(this.options.immediate) {
			var useTitles = this.options.useTitles;
			var callback = this.options.onElementValidate;
			Form.getElements(this.form).each(function(input) { // Thanks Mike!
				Event.observe(input, 'blur', function(ev) { Validation.validateElement(Event.element(ev),{useTitle : useTitles, onElementValidate : callback}); });
			});
		}
	},
	onSubmit :  function(ev){
		if(!this.validate()) Event.stop(ev);
	},
	validate : function() {
		var result = false;
		var useTitles = this.options.useTitles;
		var callback = this.options.onElementValidate;
		if(this.options.stopOnFirst) {
			result = Form.getElements(this.form).all(function(elm) { return Validation.validateElement(elm,{useTitle : useTitles, onElementValidate : callback}); });
		} else {
			result = Form.getElements(this.form).collect(function(elm) { return Validation.validateElement(elm,{useTitle : useTitles, onElementValidate : callback}); }).all();
		}
		if(!result && this.options.focusOnError) {
			var first = Form.getElements(this.form).findAll(function(elm){return $(elm).hasClassName('validation-failed')}).first();
			if(first.select) first.select();
			first.focus();
		}
		this.options.onFormValidate(result, this.form);
		return result;
	},
	reset : function() {
		Form.getElements(this.form).each(Validation.reset);
	}
}

Object.extend(Validation, {
	validateElement : function(elm, options){
		options = Object.extend({
			useTitle : false,
			onElementValidate : function(result, elm) {}
		}, options || {});
		elm = $(elm);
		var cn = elm.classNames();
		return result = cn.all(function(value) {
			var test = Validation.test(value,elm,options.useTitle);
			options.onElementValidate(test, elm);
			return test;
		});
	},
	newErrorMsgAdvice : function(name,elm,errorMsg) {
		var advice = '<div class="validation-advice" id="advice-' + name + '-' + ValidationUtils.getElmID(elm) +'" style="display:none">' + errorMsg + '</div>'
		switch (elm.type.toLowerCase()) {
			case 'checkbox':
			case 'radio':
				var p = elm.parentNode;
				if(p) {
					new Insertion.Bottom(p, advice);
				} else {
					new Insertion.After(elm, advice);
				}
				break;
			default:
				new Insertion.After(elm, advice);
	    }
		advice = $('advice-' + name + '-' + ValidationUtils.getElmID(elm));
		return advice;
	},
	showErrorMsg : function(name,elm,errorMsg) {
		var elm = $(elm);
		var prop = Validation._getAdviceProp(name);
		var advice = Validation.getAdvice(name, elm);
		if(!elm[prop]) {
			if(!advice) {
				advice = Validation.newErrorMsgAdvice(name,elm,errorMsg);
			}
			if(typeof Effect == 'undefined') {
				advice.style.display = 'block';
			} else {
				new Effect.Appear(advice, {duration : 1 });
			}
		}
		advice.innerHTML = errorMsg;
		elm[prop] = true;
		elm.removeClassName('validation-passed');
		elm.addClassName('validation-failed');
	},
	showErrorMsgByValidator : function(name,elm,useTitle) {
		Validation.showErrorMsg(name,elm,Validation.get(name).error(ValidationUtils.getInputValue(elm),elm,useTitle));
	},
	hideErrorMsg : function(name,elm) {
		var elm = $(elm);
		var prop = Validation._getAdviceProp(name);
		var advice = Validation.getAdvice(name, elm);
		if(advice) {
			if(typeof Effect == 'undefined')
				advice.hide()
			else 
				new Effect.Fade(advice, {duration : 1 });
		}
		
		elm[prop] = '';
		elm.removeClassName('validation-failed');
		elm.addClassName('validation-passed');
	},
	_getAdviceProp : function(validatorName) {
		return '__advice'+validatorName.camelize();
	},
	test : function(name, elm, useTitle) {
		var v = Validation.get(name);
		if(ValidationUtils.isVisible(elm) && !v.test(ValidationUtils.getInputValue(elm),elm)) {
			Validation.showErrorMsgByValidator(name,elm,useTitle);
			return false;
		} else {
			Validation.hideErrorMsg(name,elm);
			return true;
		}
	},
	getAdvice : function(name, elm) {
		return Try.these(
			function(){ return $('advice-' + name + '-' + ValidationUtils.getElmID(elm)) },
			function(){ return $('advice-' + ValidationUtils.getElmID(elm)) }
		);
	},
	reset : function(elm) {
		elm = $(elm);
		var cn = elm.classNames();
		cn.each(function(value) {
			var prop = Validation._getAdviceProp(value);
			if(elm[prop]) {
				var advice = Validation.getAdvice(value, elm);
				advice.hide();
				elm[prop] = '';
			}
			elm.removeClassName('validation-failed');
			elm.removeClassName('validation-passed');
		});
	},
	add : function(className, test, options) {
		var nv = {};
		var testFun = test;
		if(test instanceof RegExp)
			testFun = function(v,elm,args,metadata){ return test.test(v); }
		nv[className] = new Validator(className, testFun, options);
		Object.extend(Validation.methods, nv);
	},
	addAllThese : function(validators) {
		$A(validators).each(function(value) {
				Validation.add(value[0], value[1], (value.length > 2 ? value[2] : {}));
		});
	},
	get : function(name) {
		var resultMethodName;
		for(var methodName in Validation.methods) {
			if(name == methodName) {
				resultMethodName = methodName;
				break;
			}
			if(name.indexOf(methodName) >= 0) {
				resultMethodName = methodName;
			}
		}
		return Validation.methods[resultMethodName] ? Validation.methods[resultMethodName] : new Validator();
	},
	$ : function(formId) {
		return Validation.validations[formId];
	},
	methods : {},
	validations : {}
});

ValidationUtils = {
	isVisible : function(elm) {
		while(elm && elm.tagName != 'BODY') {
			if(!$(elm).visible()) return false;
			elm = elm.parentNode;
		}
		return true;
	},
	getInputValue : function(elm) {
		var elm = $(elm);
		if(elm.type.toLowerCase() == 'file') {
			return elm.value;
		}else {
			return $F(elm);
		}
	},
	getElmID : function(elm) {
		return elm.id ? elm.id : elm.name;
	},
	format : function(str,args) {
		args = args || [];
		ValidationUtils.assert(args.constructor == Array,"ValidationUtils.format() arguement 'args' must is Array");
		var result = str
		for (var i = 0; i < args.length; i++){
			result = result.replace(/%s/, args[i]);	
		}
		return result;
	},
	// ͨ��classname���ݵĲ�������ͨ��'-'�ָ���������
	// ����ֵ����һ������singleArgument,��:validate-pattern-/[a-c]/gi,singleArgumentֵΪ/[a-c]/gi
	getArgumentsByClassName : function(prefix,className) {
		if(!className || !prefix)
			return [];
		var pattern = new RegExp(prefix+'-(\\S+)');
		var matchs = className.match(pattern);
		if(!matchs)
			return [];
		var results = [];
		results.singleArgument = matchs[1];
		var args =  matchs[1].split('-');
		for(var i = 0; i < args.length; i++) {
			if(args[i] == '') {
				if(i+1 < args.length) args[i+1] = '-'+args[i+1];
			}else{
				results.push(args[i]);
			}
		}
		return results;
	},
	assert : function(condition,message) {
		var errorMessage = message || ("assert failed error,condition="+condition);
		if (!condition) {
			alert(errorMessage);
			throw new Error(errorMessage);
		}else {
			return condition;
		}
	},
	isDate : function(v,dateFormat) {
		var MONTH = "MM";
	   	var DAY = "dd";
	   	var YEAR = "yyyy";
		var regex = '^'+dateFormat.replace(YEAR,'\\d{4}').replace(MONTH,'\\d{2}').replace(DAY,'\\d{2}')+'$';
		if(!new RegExp(regex).test(v)) return false;

		var year = v.substr(dateFormat.indexOf(YEAR),4);
		var month = v.substr(dateFormat.indexOf(MONTH),2);
		var day = v.substr(dateFormat.indexOf(DAY),2);
		
		var d = new Date(ValidationUtils.format('%s/%s/%s',[year,month,day]));
		return ( parseInt(month, 10) == (1+d.getMonth()) ) && 
					(parseInt(day, 10) == d.getDate()) && 
					(parseInt(year, 10) == d.getFullYear() );		
	},
	//document: http://ajaxcn.org/space/start/2006-05-15/2
	fireSubmit: function(form) {
	    var form = $(form);
	    if (form.fireEvent) { //for ie
	    	if(form.fireEvent('onsubmit'))
	    		form.submit();
	    } else if (document.createEvent) { // for dom level 2
			var evt = document.createEvent("HTMLEvents");
	      	//true for can bubble, true for cancelable
	      	evt.initEvent('submit', false, true); 
	      	form.dispatchEvent(evt);
	    }
 	}
}


Validation.addAllThese([
	['required', function(v) {
				return !((v == null) || (v.length == 0) || /^\s+$/.test(v));
			},{ignoreEmptyValue:false}],
	['validate-number', function(v) {
				return (!isNaN(v) && !/^\s+$/.test(v));
			}],
	['validate-digits', function(v) {
				return !/[^\d]/.test(v);
			}],
	['validate-alphanum', function(v) {
				return !/\W/.test(v)
			}],
	['validate-one-required', function (v,elm) {
				var p = elm.parentNode;
				var options = p.getElementsByTagName('INPUT');
				return $A(options).any(function(elm) {
					return $F(elm);
				});
			},{ignoreEmptyValue : false}],
			
	['validate-digits',/^[\d]+$/],		
	['validate-alphanum',/^[a-zA-Z0-9]+$/],		
	['validate-alpha',/^[a-zA-Z]+$/],
	['validate-email',/\w{1,}[@][\w\-]{1,}([.]([\w\-]{1,})){1,3}$/],
	['validate-url',/^(http|https|ftp):\/\/(([A-Z0-9][A-Z0-9_-]*)(\.[A-Z0-9][A-Z0-9_-]*)+)(:(\d+))?\/?/i],
	// [$]1[##][,###]+[.##]
	// [$]1###+[.##]
	// [$]0.##
	// [$].##
	['validate-currency-dollar',/^\$?\-?([1-9]{1}[0-9]{0,2}(\,[0-9]{3})*(\.[0-9]{0,2})?|[1-9]{1}\d*(\.[0-9]{0,2})?|0(\.[0-9]{0,2})?|(\.[0-9]{1,2})?)$/]
]);

//custom validate start

Validation.addAllThese([
	/**
	 * Usage : equals-$otherInputId
	 * Example : equals-username or equals-email etc..
	 */
	['equals', function(v,elm,args,metadata) {
				return $F(args[0]) == v;
			},{ignoreEmptyValue:false}],
	/**
	 * Usage : less-than-$otherInputId
	 */
	['less-than', function(v,elm,args,metadata) {
				if(Validation.get('validate-number').test(v) && Validation.get('validate-number').test($F(args[0])))
					return parseFloat(v) < parseFloat($F(args[0]));
				return v < $F(args[0]);
			}],
	/**
	 * Usage : great-than-$otherInputId
	 */
	['great-than', function(v,elm,args,metadata) {
				if(Validation.get('validate-number').test(v) && Validation.get('validate-number').test($F(args[0])))
					return parseFloat(v) > parseFloat($F(args[0]));
				return v > $F(args[0]);
			}],
	/*
	 * Usage: min-length-$number
	 * Example: min-length-10
	 */
	['min-length',function(v,elm,args,metadata) {
		return v.length >= parseInt(args[0]);
	}],
	/*
	 * Usage: max-length-$number
	 * Example: max-length-10
	 */
	['max-length',function(v,elm,args,metadata) {
		return v.length <= parseInt(args[0]);
	}],
	/*
	 * Usage: validate-file-$type1-$type2-$typeX
	 * Example: validate-file-png-jpg-jpeg
	 */
	['validate-file',function(v,elm,args,metadata) {
		return $A(args).any(function(extentionName) {
			return new RegExp('\\.'+extentionName+'$','i').test(v);
		});
	}],
	/*
	 * Usage: float-range-$minValue-$maxValue
	 * Example: -2.1 to 3 = float-range--2.1-3
	 */
	['float-range',function(v,elm,args,metadata) {
		return (parseFloat(v) >= parseFloat(args[0]) && parseFloat(v) <= parseFloat(args[1]))
	},{depends : ['validate-number']}],
	/*
	 * Usage: int-range-$minValue-$maxValue
	 * Example: -10 to 20 = int-range--10-20
	 */
	['int-range',function(v,elm,args,metadata) {
		return (parseInt(v) >= parseInt(args[0]) && parseInt(v) <= parseInt(args[1]))
	},{depends : ['validate-integer']}],
	/*
	 * Usage: length-range-$minLength-$maxLength
	 * Example: 10 to 20 = length-range-10-20
	 */
	['length-range',function(v,elm,args,metadata) {
		return (v.length >= parseInt(args[0]) && v.length <= parseInt(args[1]))
	}],
	/*
	 * Usage: max-value-$number
	 * Example: max-value-10
	 */
	['max-value',function(v,elm,args,metadata) {
		return parseFloat(v) <= parseFloat(args[0]);
	},{depends : ['validate-number']}],
	/*
	 * Usage: min-value-$number
	 * Example: min-value-10
	 */
	['min-value',function(v,elm,args,metadata) {
		return parseFloat(v) >= parseFloat(args[0]);
	},{depends : ['validate-number']}],
	/*
	 * Usage: validate-pattern-$RegExp
	 * Example: <input id='sex' class='validate-pattern-/^[fm]$/i'>
	 */
	['validate-pattern',function(v,elm,args,metadata) {
		return eval('('+args.singleArgument+'.test(v))');
	}],
	/*
	 * Usage: validate-ajax-$url
	 * Example: <input id='email' class='validate-ajax-http://localhost:8080/validate-email.jsp'>
	 */
	['validate-ajax',function(v,elm,args,metadata) {
		var request = new Ajax.Request(args.singleArgument,{
			//���������URL����: validate-email.jsp?email=badqiu@gmail.com&what=email&value=badqiu@gmail.com
			//whatΪinput��name,valueΪinput��value
			parameters : Form.Element.serialize(elm)+ValidationUtils.format("&what=%s&value=%s",[elm.name,encodeURIComponent(v)]),
			asynchronous : false,
			method : "get"
		});
		
		var responseText = request.transport.responseText;
		if("" == responseText.strip()) return true;
		metadata._error = responseText;
		return false;
	}],
	/*
	 * Usage: validate-date-$dateFormat or validate-date($dateFormat default is yyyy-MM-dd)
	 * Example: validate-date-yyyy/MM/dd
	 */
	['validate-date', function(v,elm,args,metadata) {
			var dateFormat = args.singleArgument || 'yyyy-MM-dd';
			metadata._error = ValidationUtils.format(Validator.messages[metadata.className],[dateFormat,dateFormat.replace('yyyy','2006').replace('MM','03').replace('dd','12')]);
			return ValidationUtils.isDate(v,dateFormat);
		}],	
	['validate-integer',/^[-+]?[1-9]\d*$|^0$/],
	['validate-ip',/^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/],
	
	//�й������֤��ʼ
	['validate-id-number',function(v,elm,args,metadata) {
		if(!(/^\d{17}(\d|x)$/i.test(v) || /^\d{15}$/i.test(v))) return false;
		var provinceCode = parseInt(v.substr(0,2));
		if((provinceCode < 11) || (provinceCode > 91)) return false;
		var forTestDate = v.length == 18 ? v : v.substr(0,6)+"19"+v.substr(6,15);
		var birthday = forTestDate.substr(6,8);
		if(!ValidationUtils.isDate(birthday,'yyyyMMdd')) return false;
		if(v.length == 18) {
			v = v.replace(/x$/i,"a");
			var verifyCode = 0;
			for(var i = 17;i >= 0;i--)   
            	verifyCode += (Math.pow(2,i) % 11) * parseInt(v.charAt(17 - i),11);
            if(verifyCode % 11 != 1) return false;
		}
		return true;
	}],
	['validate-chinese',/^[\u4e00-\u9fa5]+$/],
	['validate-phone',/^((0[1-9]{3})?(0[12][0-9])?[-])?\d{6,8}$/],
	['validate-mobile-phone',/(^0?[1][35][0-9]{9}$)/],
	['validate-zip',/^[1-9]\d{5}$/],
	['validate-qq',/^[1-9]\d{4,8}$/]
]);

Validation.autoBind = function() {
	 var forms = document.getElementsByClassName('required-validate');
	 $A(forms).each(function(form){
		var validation = new Validation(form,{immediate:true,useTitles:true});
		Event.observe(form,'reset',function() {validation.reset();},false);
		Form.focusFirstElement(form);
	 });
};

Event.observe(window,'load',Validation.autoBind,false);