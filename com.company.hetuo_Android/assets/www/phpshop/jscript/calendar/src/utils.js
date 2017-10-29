/*
* The Zapatec DHTML utils library
*
* Copyright (c) 2004-2005 by Zapatec, Inc.
* http://www.zapatec.com
* 1700 MLK Way, Berkeley, California,
* 94709, U.S.A.
* All rights reserved.
* $Id: utils.js 2245 2006-03-24 03:30:17Z ken $
*
*
* Various utility functions
*/

// This can be defined in other modules
if (typeof Zapatec == 'undefined') {
  /// define the global Zapatec namespace
  Zapatec = {};
}

/// define the Utils namespace
Zapatec.Utils = {};

/// Retrieves the absolute position (relative to <body>) of a given element.
///
/// If it doesn't work in IE 6, try this:
/// \code
/// setTimeout(function() {
///   var objPos = Zapatec.Utils.getAbsolutePos(objElement);
///   do something with objPos
/// }, 0);
/// \endcode
///
/// @param el [HTMLElement] reference to the element.
/// @return [object] { x, y } containing the position.
Zapatec.Utils.getAbsolutePos = function(el) {
	var SL = 0, ST = 0;
	var is_div = /^div$/i.test(el.tagName);
	if (is_div && el.scrollLeft)
		SL = el.scrollLeft;
	if (is_div && el.scrollTop)
		ST = el.scrollTop;
	var r = { x: el.offsetLeft - SL, y: el.offsetTop - ST };
	if (el.offsetParent) {
		var tmp = this.getAbsolutePos(el.offsetParent);
		r.x += tmp.x;
		r.y += tmp.y;
	}
	return r;
};

/// Modify the position of a box to fit in browser's view.  This function will
/// modify the passed object itself, so it doesn't need to return a value.
///
/// @param [object] box { x, y, width, height } specifying the area.
Zapatec.Utils.fixBoxPosition = function(box) {
	if (box.x < 0)
		box.x = 0;
	if (box.y < 0)
		box.y = 0;
	var cp = Zapatec.Utils.createElement("div");
	var s = cp.style;
	s.position = "absolute";
	s.right = s.bottom = s.width = s.height = "0px";
	window.document.body.appendChild(cp);
	var br = Zapatec.Utils.getAbsolutePos(cp);
	window.document.body.removeChild(cp);
	if (Zapatec.is_ie) {
		br.y += window.document.body.scrollTop;
		br.x += window.document.body.scrollLeft;
	} else {
		br.y += window.scrollY;
		br.x += window.scrollX;
	}
	var tmp = box.x + box.width - br.x;
	if (tmp > 0) box.x -= tmp;
	tmp = box.y + box.height - br.y;
	if (tmp > 0) box.y -= tmp;
};

/// Determines if an event is related to a certain element.  This is a poor
/// substitute for some events that are missing from DOM since forever (like
/// onenter, onleave, which MSIE provides).  Basically onmouseover and
/// onmouseout are fired even if the mouse was already in the element but moved
/// from text to a blank area, so in order not to close a popup element when
/// onmouseout occurs in this situation, one would need to first check if the
/// event is not related to that popup element:
///
/// \code
///      function handler_onMouseOut(event) {
///         if (!Zapatec.Utils.isRelated(this, event)) {
///            /// can safely hide it now
///            this.style.display = "none";
///         }
///      }
/// \endcode
///
/// @param el [HTMLElement] reference to the element to check the event against
/// @param evt [Event] reference to the Event object
/// @return [boolean] true if the event is related to the element
Zapatec.Utils.isRelated = function (el, evt) {
	evt || (evt = window.event);
	var related = evt.relatedTarget;
	if (!related) {
		var type = evt.type;
		if (type == "mouseover") {
			related = evt.fromElement;
		} else if (type == "mouseout") {
			related = evt.toElement;
		}
	}
	try {
		while (related) {
			if (related == el) {
				return true;
			}
			related = related.parentNode;
		}
	} catch(e) {};
	return false;
};

/// Remove a certain [CSS] class from the given element.
/// @param el [HTMLElement] reference to the element.
/// @param className [string] the class to remove.
Zapatec.Utils.removeClass = function(el, className) {
	if (!(el && el.className)) {
		return;
	}
	var cls = el.className.split(" ");
	var ar = [];
	for (var i = cls.length; i > 0;) {
		if (cls[--i] != className) {
			ar[ar.length] = cls[i];
		}
	}
	el.className = ar.join(" ");
};

/// Appends a certain [CSS] class to the given element.
/// @param el [HTMLElement] reference to the element.
/// @param className [string] the class to append.
Zapatec.Utils.addClass = function(el, className) {
	Zapatec.Utils.removeClass(el, className);
	el.className += " " + className;
};

/// Retrieves the current target element for some event (useful when bubbling).
/// This function is not actually very useful, but it's legacy from the old calendar code.
/// @param ev [Event] the event object.
/// @return [HTMLElement] window.event.srcElement for MSIE, ev.currentTarget for other browsers.
Zapatec.Utils.getElement = function(ev) {
	if (Zapatec.is_ie) {
		return window.event.srcElement;
	} else {
		return ev.currentTarget;
	}
};

/// Retrieves the target element for some event (useful when bubbling).
/// This function is not actually very useful, but it's legacy from the old calendar code.
/// @param ev [Event] the event object.
/// @return [HTMLElement] window.event.srcElement for MSIE, ev.target for other browsers.
Zapatec.Utils.getTargetElement = function(ev) {
	if (Zapatec.is_ie) {
		return window.event.srcElement;
	} else {
		return ev.target;
	}
};

/// Stops bubbling and propagation of some event.
/// @param ev [Event] the event object
/// @return false
Zapatec.Utils.stopEvent = function(ev) {
	ev || (ev = window.event);
	if (ev) {
		if (Zapatec.is_ie) {
			ev.cancelBubble = true;
			ev.returnValue = false;
		} else {
			ev.preventDefault();
			ev.stopPropagation();
		}
	}
	return false;
};

/// Adds an event handler to a certain element.  This function adds a handler
/// using the DOM2 addEventListener (or attachEvent for MSIE).  Doing this
/// means that you can add multiple handlers for the same element and same
/// event name, and they will be called in order.
///
/// WARNING: for really old browsers that don't support attachEvent nor
/// addEventListener, it falls back to the default way: el.onclick = func.
/// This means that you CANNOT add multiple handlers in those browsers, as a
/// new one will override the old one.
///
/// @param el [HTMLElement] reference to the element.
/// @param evname [string] the event name, excluding the "on" prefix.
/// @param func event handler function.
Zapatec.Utils.addEvent = function(el, evname, func) {
	if (el.attachEvent) { // IE
		el.attachEvent("on" + evname, func);
	} else if (el.addEventListener) { // Gecko / W3C
		el.addEventListener(evname, func, false);
	} else {
		el["on" + evname] = func;
	}
};

/// Removes an event handler added with Zapatec.Utils.removeEvent().  The
/// prototype scheme is the same.
Zapatec.Utils.removeEvent = function(el, evname, func) {
	if (el.detachEvent) { // IE
		el.detachEvent("on" + evname, func);
	} else if (el.removeEventListener) { // Gecko / W3C
		el.removeEventListener(evname, func, false);
	} else {
		el["on" + evname] = null;
	}
};

/// Create an element of a certain type using document.createElement().  A
/// function was needed in order to add some common attributes to all created
/// elements, but also in order to be able to use it in XHTML too (Gecko and
/// other W3C-compliant browsers).
///
/// This function will create an element of the given type and set certain
/// properties to it: unselectable for IE, and the CSS "-moz-user-select" for
/// Gecko, in order to make the element unselectable in these browsers.
/// Optionally, if the second argument is passed, it will appendChild() the
/// newly created element to its parent.
///
/// @param type [string] the tag name of the new element.
/// @param parent [HTMLElement, optional] a parent for the new element.
/// @param selectable [boolean] the flag to indicate wether element is selectable(rather usefull).
/// @return [HTMLElement] reference to the new element.
Zapatec.Utils.createElement = function(type, parent, selectable) {
	var el = null;
	if (window.self.document.createElementNS)
		// use the XHTML namespace; IE won't normally get here unless
		// _they_ "fix" the DOM2 implementation.
		el = window.self.document.createElementNS("http://www.w3.org/1999/xhtml", type);
	else
		el = window.self.document.createElement(type);
	if (typeof parent != "undefined" &&parent != null)
		parent.appendChild(el);
	if (!selectable) {
		if (Zapatec.is_ie)
			el.setAttribute("unselectable", true);
		if (Zapatec.is_gecko)
			el.style.setProperty("-moz-user-select", "none", "");
	}
	return el;
};

// Cookie management

/// Sets a cooke given certain specifications.  It overrides any existing
/// cookie with the same name.
///
/// @param name [string] the cookie name.
/// @param value [string] the cookie value.
/// @param domain [string, optional] the cookie domain.
/// @param path [string, optional] the cookie path.
/// @param exp_days [number, optional] number of days of cookie validity.
Zapatec.Utils.writeCookie = function(name, value, domain, path, exp_days) {
	value = escape(value);
	var ck = name + "=" + value, exp;
	if (domain)
		ck += ";domain=" + domain;
	if (path)
		ck += ";path=" + path;
	if (exp_days) {
		exp = new Date();
		exp.setTime(exp_days * 86400000 + exp.getTime());
		ck += ";expires=" + exp.toGMTString();
	}
	document.cookie = ck;
};

/**
 * Retrieves the value of a cookie.
 *
 * @param name [string] the cookie name
 * @return [string or null] a string with the cookie value, or null if it can't be found.
 */

/* ? inside regular expression is not supported in IE 5.0
Zapatec.Utils.getCookie = function(name) {
	var re = new RegExp("(^|;\\s*)" + name + "\\s*=(.*?)(;|$)");
	if (re.test(document.cookie)) {
		var value = RegExp.$2;
		value = unescape(value);
		return (value);
	}
	return null;
};
*/

Zapatec.Utils.getCookie = function(name) {
	var pattern = name + "=";
	var tokenPos = 0;
	while (tokenPos < document.cookie.length) {
		var valuePos = tokenPos + pattern.length;
		if (document.cookie.substring(tokenPos, valuePos) == pattern) {
			var endValuePos = document.cookie.indexOf(";", valuePos);
			if (endValuePos == -1) { // Last cookie
				endValuePos = document.cookie.length;
			}
			return unescape(document.cookie.substring(valuePos, endValuePos));
		}
		tokenPos=document.cookie.indexOf(" ",tokenPos)+1;
		if (tokenPos == 0) { // No more tokens
			break;
		}
	}
	return null;
};

/**
 * Given an object, create a string suitable for saving the object in a cookie.
 * This is similar to serialization.  WARNING: it does not support nested
 * objects.
 *
 * @param obj [Object] reference to the object to serialize.
 * @return [string] the serialized object.
 */
Zapatec.Utils.makePref = function(obj) {
	function stringify(val) {
		if (typeof val == "object" && !val)
			return "null";
		else if (typeof val == "number" || typeof val == "boolean")
			return val;
		else if (typeof val == "string")
			return '"' + val.replace(/\22/, "\\22") + '"';
		else return null;
	};
	var txt = "", i;
	for (i in obj)
		txt += (txt ? ",'" : "'") + i + "':" + stringify(obj[i]);
	return txt;
};

/**
 * The reverse of Zapatec.Utils.makePref(), this function unserializes the
 * given string and creates an object from it.
 *
 * @param txt [string] the serialized value.
 * @return [Object] a new object if it was created successfully or null otherwise.
 */
Zapatec.Utils.loadPref = function(txt) {
	var obj = null;
	try {
		eval("obj={" + txt + "}");
	} catch(e) {}
	return obj;
};

/**
 * Merges the values of the source object into the destination object.
 *
 * @param dest [Object] the destination object.
 * @param src [Object] the source object.
 */
Zapatec.Utils.mergeObjects = function(dest, src) {
	for (var i in src)
		dest[i] = src[i];
};

// based on the WCH idea
// http://www.aplus.co.yu/WCH/code3/WCH.js

/// \defgroup WCH functions
//@{

Zapatec.Utils.__wch_id = 0;	/**< [number, static] used to create ID-s for the WCH objects */

/**
 * Create an WCH object.  This function does nothing if the browser is not
 * IE5.5 or IE6.0.  A WCH object is one of the most bizarre tricks to avoid a
 * notorious IE bug: IE normally shows "windowed controls" on top of any HTML
 * elements, regardless of any z-index that might be specified in CSS.  This
 * technique is described at: http://www.aplus.co.yu/WCH/
 *
 * A "WCH object" is actually an HTMLIFrame element having a certain "CSS
 * filter" (proprietary MSIE extension) that forces opacity zero.  This object,
 * displayed on top of a windowed control such as a select box, will completely
 * hide the select box, allowing us to place other HTMLElement objects above.
 *
 * WCH stands for "Windowed Controls Hider".
 *
 * @param element [HTMLElement, optional] -- Create the WCH IFRAME inside this.
 *
 *
 * @return [HTMLIFrame or null] a new WCH object if the browser is "supported", null otherwise.
 */
Zapatec.Utils.createWCH = function(element) {
	var f = null;
	element = element || window.self.document.body;
	if (Zapatec.is_ie && !Zapatec.is_ie5) {
		var filter = 'filter:progid:DXImageTransform.Microsoft.alpha(style=0,opacity=0);';
		var id = "WCH" + (++Zapatec.Utils.__wch_id);
		element.insertAdjacentHTML
			('beforeEnd', '<iframe id="' + id + '" scroll="no" frameborder="0" ' +
			 'style="z-index:0;position:absolute;visibility:hidden;' + filter +
			 'border:0;top:0;left:0;width:0;height:0;" ' +
			 'src="javascript:false;"></iframe>');
		f = window.self.document.getElementById(id);
	}
	return f;
};

/**
 * Configure a given WCH object to be displayed on top of the given element.
 * Optionally, a second element can be passed, and in this case it will setup
 * the WCH object to cover both elements.
 *
 * @param f [HTMLIFrame] the WCH object
 * @param el [HTMLElement] the element to cover.
 * @param el2 [HTMLElement, optional] another element to cover.
 */
Zapatec.Utils.setupWCH_el = function(f, el, el2) {
	if (f) {
		var pos = Zapatec.Utils.getAbsolutePos(el),
			X1 = pos.x,
			Y1 = pos.y,
			X2 = X1 + el.offsetWidth,
			Y2 = Y1 + el.offsetHeight;
		if (el2) {
			var p2 = Zapatec.Utils.getAbsolutePos(el2),
				XX1 = p2.x,
				YY1 = p2.y,
				XX2 = XX1 + el2.offsetWidth,
				YY2 = YY1 + el2.offsetHeight;
			if (X1 > XX1)
				X1 = XX1;
			if (Y1 > YY1)
				Y1 = YY1;
			if (X2 < XX2)
				X2 = XX2;
			if (Y2 < YY2)
				Y2 = YY2;
		}
		Zapatec.Utils.setupWCH(f, X1, Y1, X2-X1, Y2-Y1);
	}
};

/**
 * Configure a WCH object to cover a certain part of the screen.
 *
 * @param f [HTMLIFrame] the WCH object.
 * @param x [number] the X coordinate.
 * @param y [number] the Y coordinate.
 * @param w [number] the width of the area.
 * @param h [number] the height of the area.
 */
Zapatec.Utils.setupWCH = function(f, x, y, w, h) {
	if (f) {
		var s = f.style;
		(typeof x != "undefined") && (s.left = x + "px");
		(typeof y != "undefined") && (s.top = y + "px");
		(typeof w != "undefined") && (s.width = w + "px");
		(typeof h != "undefined") && (s.height = h + "px");
		s.visibility = "inherit";
	}
};

/**
 * Hide a WCH object.
 *
 * @param f [HTMLIFrame] object to hide.
 */
Zapatec.Utils.hideWCH = function(f) {
	if (f)
		f.style.visibility = "hidden";
};

//@}

/// \defgroup Scroll-with-window functions
//@{

/**
 * A generic Utils function that returns the current scroll position.
 *
 */
Zapatec.Utils.getPageScrollY = function() {
	return window.pageYOffset ||
			document.documentElement.scrollTop ||
			(document.body ? document.body.scrollTop : 0) ||
			0;
};

// Object setup.
Zapatec.ScrollWithWindow = {};
Zapatec.ScrollWithWindow.list = [];
// Set to a number between 0 and 1, lower means longer scrolling.
Zapatec.ScrollWithWindow.stickiness = 0.25;

/**
 * Registers a given object to have its style.top set equal to the window
 * scroll position as the browser scrolls.
 *
 * @param node [HTMLElement] -- a reference to the node to scroll.
 */
Zapatec.ScrollWithWindow.register = function(node) {
	var top = parseInt(node.style.top) || 0;
	var scrollY = window.pageYOffset || document.body.scrollTop ||
		document.documentElement.scrollTop || 0;
	top -= scrollY;
	if (top < 0) top = 0;
	Zapatec.ScrollWithWindow.list[Zapatec.ScrollWithWindow.list.length] = {
		node: node,
		origTop: top
	};
};

/**
 * Unregisters a given object.
 *
 * @param node [HTMLElement] -- a reference to the node to scroll.
 */
Zapatec.ScrollWithWindow.unregister = function(node) {
	for (var count = 0; count < Zapatec.ScrollWithWindow.list.length; count++) {
		var elm = Zapatec.ScrollWithWindow.list[count];
		if (node == elm.node) {
			Zapatec.ScrollWithWindow.list.splice(count, 1);
			return;
		}
	}
};

/**
 * \internal Called each time the window is scrolled to set objects' positions.
 *
 * @param newScrollY [number] -- the new window scroll position.
 */
Zapatec.ScrollWithWindow.handler = function(newScrollY) {
	// Move oldScrollY towards newScrollY, evening up if the difference is small.
	oldScrollY += ((newScrollY - oldScrollY) * this.stickiness);
	if (Math.abs(oldScrollY - newScrollY) <= 1) oldScrollY = newScrollY;
	for (var count = 0; count < Zapatec.ScrollWithWindow.list.length; count++) {
		var elm = Zapatec.ScrollWithWindow.list[count];
		var node = elm.node;
		if (!elm.origTop) {
			elm.origTop = Zapatec.Utils.getAbsolutePos(node).y;
			node.style.position = 'absolute';
		}
		node.style.top = elm.origTop + parseInt(oldScrollY) + 'px';
	}
};

// Processed scroll position & Event hook.
var oldScrollY = Zapatec.Utils.getPageScrollY();
setInterval(
	'var newScrollY = Zapatec.Utils.getPageScrollY(); ' +
	'if (newScrollY != oldScrollY) { ' +
		'Zapatec.ScrollWithWindow.handler(newScrollY); ' +
	'}', 50);

//@}

/**
 * Destroys the given element (remove it from the DOM tree) if it's not null
 * and it's parent is not null.
 *
 * @param el [HTMLElement] the element to destroy.
 */
Zapatec.Utils.destroy = function(el) {
	if (el && el.parentNode)
		el.parentNode.removeChild(el);
};

/**
 * Opens a new window at a certain URL and having some properties.
 *
 * @param url [string] the URL to open a new window to.
 * @param windowName [string] the name of the new window (as for target attribute).
 * @param width [number] the width of the new window in pixels.
 * @param height [number] the height of the new window in pixels.
 * @param scrollbars [string] "yes" or "no" for scrollbars.
 *
 * @return [object] the new window
 */
Zapatec.Utils.newCenteredWindow = function(url, windowName, width, height, scrollbars){
	var leftPosition = 0;
	var topPosition = 0;
	if (screen.width)
		leftPosition = (screen.width -  width)/2;
	if (screen.height)
		topPosition = (screen.height -  height)/2;
	var winArgs =
		'height=' + height +
		',width=' + width +
		',top=' + topPosition +
		',left=' + leftPosition +
		',scrollbars=' + scrollbars +
		',resizable';
	var win = window.open(url,windowName,winArgs);
	return win;
};

/**
 * Finds the size of the current web page. This is the usable size
 * and does not include the browser's menu and buttons.
 *
 * @return [object] dimension with the height and width of the window
 */
Zapatec.Utils.getWindowSize = function() {
  var iWidth = 0;
  var iHeight = 0;
  if (document.compatMode && document.compatMode == 'CSS1Compat') {
    // Standards-compliant mode
    if (window.opera) {
    	iWidth = document.body.clientWidth || 0;
    	iHeight = document.body.clientHeight || 0;
    } else {
    	iWidth = document.documentElement.clientWidth || 0;
    	iHeight = document.documentElement.clientHeight || 0;
    }
  } else {
    // Non standards-compliant mode
  	iWidth = window.innerWidth || document.body.clientWidth ||
  	 document.documentElement.clientWidth || 0;
  	iHeight = window.innerHeight || document.body.clientHeight ||
  	 document.documentElement.clientHeight || 0;
  }
	return {
	  width: iWidth,
	  height: iHeight
	};
};


/**
 * Given a reference to a select element, this function will select the option
 * having the given value and optionally will call the default handler for
 * "onchange".
 *
 * @param sel [HTMLSelectElement] reference to the SELECT element.
 * @param val [string] the value that we should select.
 * @param call_default [boolean] true if the default onchange should be called.
 */
Zapatec.Utils.selectOption = function(sel, val, call_default) {
	var a = sel.options, i, o;
	for (i = a.length; --i >= 0;) {
		o = a[i];
		o.selected = (o.val == val);
	}
	sel.value = val;
	if (call_default) {
		if (typeof sel.onchange == "function")
			sel.onchange();
		else if (typeof sel.onchange == "string")
			eval(sel.onchange);
	}
};

/**
 * A more flexible way to get the "nextSibling" of a given element.  If the
 * "tag" argument is passed, then this function will return the next sibling
 * that has a certain tag.  Otherwise it will simply return el.nextSibling.
 *
 * @param el [HTMLElement] reference to the anchor element.
 * @param tag [string] the tag name of the returned node.
 * @param alternateTag [string] the alternate tag name of the returned node.
 *
 * @return [HTMLElement or null] el.nextSibling if tag is not passed, or the
 * first element after el having the specified tag.  Null is returned if no
 * element could be found.
 */
Zapatec.Utils.getNextSibling = function(el, tag, alternateTag) {
	el = el.nextSibling;
	if (!tag) {
		return el;
	}
	tag = tag.toLowerCase();
	if (alternateTag) alternateTag = alternateTag.toLowerCase();
	while (el) {
		if (el.nodeType == 1 && (el.tagName.toLowerCase() == tag ||
		 (alternateTag && el.tagName.toLowerCase() == alternateTag))) {
			return el;
		}
		el = el.nextSibling;
	}
	return el;
};

/**
 * Similar to Zapatec.Utils.getNextSibling(), this function will return the
 * first child of the given element that has a specified tag.
 *
 * @param el [HTMLElement] reference to the anchor element.
 * @param tag [string] the tag name of the returned node.
 * @param alternateTag [string] the alternate tag name of the returned node.
 *
 * @return [HTMLElement] reference to the found node, or null if none could be
 * found.
 */
Zapatec.Utils.getFirstChild = function(el, tag, alternateTag) {
  if (!el) {
    return null;
  }
	el = el.firstChild;
  if (!el) {
    return null;
  }
	if (!tag) {
		return el;
	}
	tag = tag.toLowerCase();
	if (el.nodeType == 1) {
		if (el.tagName.toLowerCase() == tag) {
			return el;
		} else if (alternateTag) {
			alternateTag = alternateTag.toLowerCase();
			if (el.tagName.toLowerCase() == alternateTag) {
				return el;
			}
		}
	}
	return Zapatec.Utils.getNextSibling(el, tag, alternateTag);
};

/**
 * Function that concatenates and returns all text child nodes of the
 * specified node.
 *
 * @param objNode [Node] -- reference to the node.
 * @return [string] -- concatenated text child nodes
 */
Zapatec.Utils.getChildText = function(objNode) {
	if (objNode == null) {
		return '';
	}
	var arrText = [];
	var objChild = objNode.firstChild;
	while (objChild != null) {
		if (objChild.nodeType == 3) { // Node.TEXT_NODE
			arrText.push(objChild.data);
		}
		objChild = objChild.nextSibling;
	}
	return arrText.join(' ');
};

/**
 * Similar to the DOM's built in insertBefore.
 * Insert a node after an existing node.
 *
 * @param el [oldNode] The existing element
 * @param el [newNode] the new element to insert after the old one.
 *
 */
Zapatec.Utils.insertAfter = function(oldNode, newNode) {
	if(oldNode.nextSibling) {
		oldNode.parentNode.insertBefore(newNode, oldNode.nextSibling);
	} else {
		oldNode.parentNode.appendChild(newNode);
	}
}

Zapatec.Utils._ids = {};	/**< [number, static] maintains a list of generated IDs */

/**
 * Generates an unique ID, for a certain code (let's say "class").  If the
 * optional "id" argument is passed, then it just returns the id for that code
 * (no generation).  This function is sometimes useful when we need to create
 * elements and be able to access them later by ID.
 *
 * @param code [string] the class of ids.  User defined, can be anything.
 * @param id [string, optional] specify if the ID is already known.
 *
 * @return [string] the unique ID
 */
Zapatec.Utils.generateID = function(code, id) {
	if (typeof id == "undefined") {
		if (typeof this._ids[code] == "undefined")
			this._ids[code] = 0;
		id = ++this._ids[code];
	}
	return "zapatec-" + code + "-" + id;
};

/**
*  Add a tooltip to the specified element.
*
*  Function that adds a custom tooltip for an element.  The "target" is the
*  element to where the tooltip should be added to, and the "tooltip" is a DIV
*  that contains the tooltip text.  Optionally, the tooltip DIV can have the
*  "title" attribute set; if so, its value will be displayed highlighted as
*  the title of the tooltip.
*
*  @param target  reference to or ID of the target element
*  @param tooltip reference to or ID of the tooltip content element
*/

Zapatec.Utils.addTooltip = function(target, tooltip) {
return new Zapatec.Tooltip(target, tooltip);
};

Zapatec.isLite=true;

Zapatec.Utils.checkActivation = function() {
	if (!Zapatec.isLite)	return true;

	var arrProducts=[]

	add_product=function(script, webdir_in, name_in)
	{
	arrProducts[script]={webdir:webdir_in, name:name_in, bActive:false}
	}

	add_product('calendar.js', 'prod1',   'Calendar')
	add_product('menu.js',     'prod2',   'Menu')
	add_product('tree.js',     'prod3',   'Tree')
	add_product('form.js',     'forms',   'Forms')
	add_product('effects.js',  'effects', 'Effects')
	add_product('hoverer.js',  'effects', 'Effects - Hoverer')
	add_product('slideshow.js','effects', 'Effects - Slidshow')
	add_product('zpgrid.js',   'grid',    'Grid')
	add_product('slider.js',   'slider',  'Slider')
	add_product('zptabs.js',   'tabs',    'Tabs')
	add_product('zptime.js',   'time',    'Time')
	add_product('window.js',   'windows', 'Window')


	var strName, arrName, i
	var bProduct=false // Flag yes if we have a zapatec script
	var scripts = document.getElementsByTagName('script');
	for (i=0; i<scripts.length; i++)
	{
		// If wizard then do NOT do link back check, which makes wizard err out
		if (/wizard.js/i.test(scripts[i].src))
			return true

		arrName=scripts[i].src.split('/')
		if (arrName.length==0)
			strName=scripts[i]
		else
			strName=arrName[arrName.length-1]
		strName=strName.toLowerCase()
		// Get each active product
		if (typeof arrProducts[strName] != 'undefined')
			{
			bProduct=true
			arrProducts[strName].bActive=true
			}
	}

	// Is a LITE product even being used?
	if (!bProduct) return true;


	var anchors = document.getElementsByTagName('A');
	for(i = 0; i < anchors.length; i++)
		if (/(dev|www)\.zapatec\.com/i.test(anchors[i].href))
			return true;

	var strMsg='You are using the Free version of the Zapatec Software.\n'+
	'While using the Free version, a link to www.zapatec.com in this page is required.'

	for (i in arrProducts)
		// Get each active product
		if (arrProducts[i].bActive==true)
			strMsg+='\nTo purchase the Zapatec ' + arrProducts[i].name + ' visit www.zapatec.com/website/main/products/' + arrProducts[i].webdir + '/'

	alert(strMsg)

	return false;
}

// Browser sniffing

/// detect Opera browser
Zapatec.is_opera = /opera/i.test(navigator.userAgent);

/// detect a special case of "web browser"
Zapatec.is_ie = ( /msie/i.test(navigator.userAgent) && !Zapatec.is_opera );

/// detect IE5.0/Win
Zapatec.is_ie5 = ( Zapatec.is_ie && /msie 5\.0/i.test(navigator.userAgent) );

/// detect IE for Macintosh
Zapatec.is_mac_ie = ( /msie.*mac/i.test(navigator.userAgent) && !Zapatec.is_opera );

/// detect KHTML-based browsers
Zapatec.is_khtml = /Konqueror|Safari|KHTML/i.test(navigator.userAgent);

/// detect Konqueror
Zapatec.is_konqueror = /Konqueror/i.test(navigator.userAgent);

/// detect Gecko
Zapatec.is_gecko = /Gecko/i.test(navigator.userAgent);

/**
 * Simulation of Function call() method that is missing in IE 5.0.
 */
if (!Function.prototype.call) {
	Function.prototype.call = function () {
		var self = arguments[0];
		self._this_func = this;
		var args = new Array();
		for (var i=1; i < arguments.length; i++) {
			args[args.length] = 'arguments[' + i + ']';
		}
		var ret = eval('self._this_func(' + args.join(',') + ')');
		self._this_func = null;
		return ret;
	};
}

/**
 * Simulation of Array pop() method that is missing in IE 5.0.
 */
if (!Array.prototype.pop) {
	Array.prototype.pop = function() {
		var last;
		if (this.length) {
			last = this[this.length - 1];
			this.length -= 1;
		}
		return last;
	};
}

/**
 * Simulation of Array push() method that is missing in IE 5.0
 */
if (!Array.prototype.push) {
	Array.prototype.push = function() {
		for (var i = 0; i < arguments.length; i++) {
			this[this.length] = arguments[i];
		}
		return this.length;
	};
}

/**
 * Simulation of Array shift() method that is missing in IE 5.0.
 */
if (!Array.prototype.shift) {
	Array.prototype.shift = function() {
		var first;
		if (this.length) {
			first = this[0];
			for (var i = 0; i < this.length - 1; i++) {
				this[i] = this[i + 1];
			}
			this.length -= 1;
		}
		return first;
	};
}

/**
 * Simulation of Array unshift() method that is missing in IE 5.0.
 */
if (!Array.prototype.unshift) {
	Array.prototype.unshift = function() {
		if (arguments.length) {
			var i, len = arguments.length;
			for (i = this.length + len - 1; i >= len; i--) {
				this[i] = this[i - len];
			}
			for (i = 0; i < len; i++) {
				this[i] = arguments[i];
			}
		}
		return this.length;
	};
}

/**
 * Simulation of Array splice() method that is missing in IE 5.0.
 */
if (!Array.prototype.splice) {
	Array.prototype.splice = function(index, howMany) {
		var elements = [], removed = [], i;
		for (i = 2; i < arguments.length; i++) {
			elements.push(arguments[i]);
		}
		for (i = index; (i < index + howMany) && (i < this.length); i++) {
			removed.push(this[i]);
		}
		for (i = index + howMany; i < this.length; i++) {
			this[i - howMany] = this[i];
		}
		this.length -= removed.length;
		for (i = this.length + elements.length - 1; i >= index + elements.length;
		 i--) {
			this[i] = this[i - elements.length];
		}
		for (i = 0; i < elements.length; i++) {
			this[index + i] = elements[i];
		}
		return removed;
	};
}

/**
 * Displays error message. Override this if needed.
 *
 * \param objArgs [number] error object:
 * {
 *   severity: [string, optional] error severity,
 *   description: [string] human readable error description
 * }
 */
Zapatec.Log = function(objArgs) {
  // Check arguments
  if (!objArgs) {
    return;
  }
  // Form error message
  var strMessage = objArgs.description;
  if (objArgs.severity) {
    strMessage = objArgs.severity + ':\n' + strMessage;
  }
  // Display error message
  alert(strMessage);
};

/// Zapatec.Utils.Array object which contains additional for arrays method
Zapatec.Utils.Array = {};

/**
 * Inserts the element into array. 
 * It influences the order in which the elements will be iterated in the for...in cycle.
 *
 * @param arr [array] array to work with.
 * @param el [mixed] element to insert.
 * @param key [string] element to insert.
 * @param nextKey [string] element to be inserted before.
 * @return [string] new Array.
 */
Zapatec.Utils.Array.insertBefore = function (arr, el, key, nextKey) {
	var tmp = new Array();
	for(var i in arr) {
		if (i == nextKey) {
			if (key) {
				tmp[key] = el;
			} else {
				tmp.push(el);
			}
		}
		tmp[i] = arr[i];
	}
	return tmp;
}
