var _j = -1,
        _7 = -1,
        _T = false;
function COOLjsOutlookBar(_2) {
    bw = new _14();
    window.$bar = this;
    this._2 = _2;
    this._n = false;
    this._h = 0;
    this._t = 0;
    this._w = '';
    window.onload = function() {
        window.$bar._b()
    };
    window.onunload = function() {
        window.$bar._a()
    };
    window.onresize = function() {
        if (bw.ns4) document.location.reload();
        else window.$bar._l()
    };
    document.onselect = function() {
        return false
    };
    if (bw.operaOld) {
        $iw = innerWidth;
        $ih = innerHeight;
        $tiw = top.innerWidth;
        $tih = top.innerHeight;
        window.setInterval('if($iw!=innerWidth||$ih!=innerHeight||$tiw!=top.innerWidth||$tih!=top.innerHeight)document.location.reload();', 300)
    }
    ;
    this.$panels = this._6 = [];
    this._2.panels = this._2.panels.slice(0, 7);
    for (var i = 0; i < this._2.panels.length; i++) this._6[i] = new _S(this, i);
    this._r = new _p(bw.ns4 ? null : '100%', 1000, {
        normal: '<table width="100%" cellspacing="0" cellpadding="0" bgcolor="silver"><tr><td align="center"><div style="font: 6pt small fonts, tahoma, arial; color: black;">' + unescape('%43%4F%4F%4C%6A%73%4F%75%74%6C%6F%6F%6B%42%61%72%26%6E%62%73%70%3B%50%52%4F') + '</div></td></tr></table>'
    },
    {});
    document.write('<div id="dummyNN4Layer" style="left: 0; top: 0;"></div>' + this._w)
}
;
$ = COOLjsOutlookBar.prototype;
$._O = function(_o, _q) {
    window.open(_o, _q || this._2.format.target)
};
$._b = function() {
    for (var i = 0; i < this._6.length; i++) this._6[i]._b();
    this._r._b();
    this._6[0]._Y(0)
};
$._a = function() {
    for (var i = 0; i < this._6.length; i++) this._6[i]._a();
    this._r._a()
};
$._16 = function(_11) {
    this._w += _11
};
$._$ = function() {
    if (this._K) {
        window.clearTimeout(this._K);
        this._n = false;
        this._l()
    }
    ;
    this._h = 0;
    this._n = true
};
$.$update = $._l = function(_9) {
    for (var i = 0; i < this._6.length; i++) this._6[i]._l(this._n && _9);
    if (this._n) if (this._h < 1000) {
        this._h = Math.round(Math.min(this._h + 1000 / this._2.format.animationSteps, 1000));
        this._K = window.setTimeout('window.$bar.$update(' + _9 + ')', this._2.format.animationDelay)
    } else {
        this._h = 0;
        this._n = false;
        this._K = null;
        this._l();
        if (!_9 && _7 != -1 && this._t) {
            if (!_T) this._6[_7]._B(this._t);
            _T = false
        }
    }
    ;
    this._r._i(0, this._W());
    this._r._f()
};
$._W = function() {
    return (bw.ie && document.body.offsetHeight || (innerHeight + (bw.ns4 ? 4 : 0))) - this._r.h
};
$._d = function() {
    var _g = this._W();
    for (var i = 0; i < this._6.length; i++) _g -= this._6[i]._1.h;
    return _g
};
$._V = function(_15) {
    return Math.round(_15 * (1000 - this._h) / 1000)
};
function _p(_v, _z, _P, _s) {
    this._F = 0;
    this._x = false;
    this._5 = [new _m(), null, null, new _m(true)];
    this._5[0]._M(_v, 0, _z * 2, this._R(_P.normal, _s, _P.common));
    this._5[3]._M(_v, 10, _z * 2 + 1, bw.realDom ? '<img src="' + window.$bar._2.format.blankImage + '" width="100%" height="100%" />' : '')
}
;
$ = _p.prototype;
$._b = function() {
    this._5[0]._b();
    this._5[3]._b();
    with (this._5[0]) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h
    }
    ;
    this._5[3].el._D = this;
    this._5[3].el.onmousedown = function() {
        this._18 = true;
        this._D._H()
    };
    this._5[3].el.onmouseup = function() {
        this._18 = false;
        if (bw.ns4) this.onclick();
        this._D._C()
    };
    this._5[3].el.onclick = function() {
        this._D._I()
    };
    if (bw.realDom) {
        this._5[0]._f();
        this._5[0]._e()
    }
};
$._a = function() {
    this._5[0]._a();
    this._5[3]._a()
};
$._1a = $._X = $._H = $._C = $._I = function() {
};
$._f = function() {
    if (!this._x) {
        this._5[this._F]._f();
        this._5[3]._f();
        this._x = true
    }
    ;
    this._5[3]._19(this.h = this._5[this._F]._G())
};
$._e = function() {
    if (this._x) {
        this._5[this._F]._e();
        this._5[3]._e();
        this._x = false
    }
};
$._i = function(_x, _y) {
    this._5[0]._i(_x, _y);
    this._5[3]._i(_x, _y);
    this.x = _x;
    this.y = _y
};
$._R = function(_k, _s, _17) {
    if (typeof(_k) == 'object') _k = this._R(_17, _k);
    for (var _E in _s) while (_k.indexOf('{' + _E + '}') != -1) _k = _k.replace('{' + _E + '}', _s[_E]);
    return _k
};
function _S(_0, _3) {
    this._0 = _0;
    this._3 = _3;
    this._c = 0;
    var _2 = this._0._2.panels[this._3];
    this._o = _2.url;
    this._q = _2.target;
    this._1 = new _p(bw.ns4 ? null : '100%', 3, this._0._2.format.templates.panel, _2);
    this._1._8 = this;
    this._1._I = function() {
        if (this._8._12()) {
            this._8._0._$();
            this._8._Y(0)
        }
        ;
        if (this._8._o) window.$bar._O(this._8._o, this._8._q)
    };
    this._4 = [];
    for (var i = 0; i < this._0._2.panels[this._3].items.length; i++) {
        var _2 = this._0._2.panels[this._3].items[i];
        this._4[i] = new _p(bw.ns4 ? null : '100%', 1, this._0._2.format.templates.item, _2);
        if (_2.url) {
            this._4[i]._o = _2.url;
            this._4[i]._q = _2.target;
            this._4[i]._8 = this;
            this._4[i]._I = function() {
                if (window.$bar._d() >= this.h && !this._8._u(this, true)) this._8._B(+ 1);
                window.$bar._O(this._o, this._q)
            }
        }
    }
    ;
    this.arr_up = new _p('auto', 2, this._0._2.format.templates.upArrow, {});
    this.arr_up._8 = this;
    this.arr_up._H = function() {
        this._8._B(- 1)
    };
    this.arr_dn = new _p('auto', 2, this._0._2.format.templates.downArrow, {});
    this.arr_dn._8 = this;
    this.arr_dn._H = function() {
        this._8._B(+ 1)
    };
    this.arr_up._X = this.arr_up._C = this.arr_dn._X = this.arr_dn._C = function() {
        this._8._0._t = 0
    }
}
;
$ = _S.prototype;
$._b = function() {
    this._1._b();
    for (var i = 0; i < this._4.length; i++) this._4[i]._b();
    this.arr_up._b();
    this.arr_dn._b()
};
$._a = function() {
    this._1._a();
    for (var i = 0; i < this._4.length; i++) this._4[i]._a();
    this.arr_up._a();
    this.arr_dn._a()
};
$._Z = function() {
    return bw.ie && document.body.offsetWidth || innerWidth
};
$._u = function(_1, _13) {
    if (_13) return _1.y >= this._1.y && (this._3 == this._0._6.length - 1 || _1.y + _1.h - 1 < this._0._6[this._3 + 1]._1.y + this._0._6[this._3 + 1]._1.h);
    else return (_1.y >= this._1.y + this._1.h && _1.y < this._1.y + this._1.h + this._0._d()) || (_1.y + _1.h >= this._1.y + this._1.h && _1.y + _1.h < this._1.y + this._1.h + this._0._d())
};
$._10 = function(_N) {
    var _g = 0;
    for (var i = 0; i < this._3; i++) _g += this._0._6[i]._1.h;
    if (_N >= 0 && this._3 > _N) _g += this._0._d();
    return _g
};
$.$update = $._l = function(_9) {
    var _y = this._10(_7);
    this._1._i(0, _y + (_9 ? this._0._V(this._10(_j) - _y) : 0));
    this._1._f();
    if (_7 == this._3 || (_9 && _j == this._3)) {
        if (this._y() < this._0._d()) while (this._c > 0) {
            this._c--;
            if (this._y() > this._0._d()) {
                this._c++;
                break
            } else if (this._y() >= this._0._d()) break
        }
        ;
        _y += this._1.h;
        if (this._0._h == 0) {
            this._dy = 0;
            if (_9) {
                if (this._3 == _7) {
                    if (_7 > _j && _j != -1) this._dy = this._0._d()
                } else {
                    if (_j < _7 || _7 == -1) return;
                    else this._dy = -this._0._d()
                }
            } else if (this._0._n) this._dy = this._4[this._c].y - _y
        }
        ;
        for (var i = 0; i < this._c; i++) _y -= this._4[i].h;
        _y += this._0._V(this._dy);
        for (var i = 0; i < this._4.length; i++) {
            this._4[i]._i(0, _y);
            if (this._u(this._4[i], _9)) this._4[i]._f();
            else this._4[i]._e();
            _y += this._4[i].h
        }
    } else if (_j == this._3) for (var i = 0; i < this._4.length; i++) this._4[i]._e();
    this.arr_up._i(this._Z() - this.arr_up.w, this._1.y + this._1.h);
    this.arr_dn._i(this._Z() - this.arr_dn.w, this._1.y + this._1.h + this._0._d() - this.arr_dn.h);
    if (this._U()) this.arr_up._f();
    else if (!_9 || this._0._h == 1000) this.arr_up._e();
    if (!this._u(this.arr_up, _9)) this.arr_up._e();
    if (this._L()) this.arr_dn._f();
    else if (!_9 || this._0._h == 1000) this.arr_dn._e();
    if (!this._u(this.arr_dn, _9)) this.arr_dn._e()
};
$._B = function(_J) {
    if (_J < 0 ? this._U() : this._L()) {
        this._0._t = _J;
        this._0._$();
        this._c += _J;
        this._0._l()
    }
};
$._y = function() {
    var _g = 0;
    for (var i = this._c; i < this._4.length; i++) _g += this._4[i].h;
    return _g
};
$._L = function() {
    return this._c < this._4.length - 1 && _7 == this._3 && this._y() > this._0._d()
};
$._U = function() {
    return this._c > 0 && _7 == this._3
};
$._12 = function() {
    return _7 != this._3 || this._0._2.format.rollback
};
$._Y = function(_c) {
    if (_7 == this._3) {
        if (this._0._2.format.rollback) {
            _j = this._3;
            _7 = -1
        } else return
    } else {
        _j = _7;
        _7 = this._3
    }
    ;
    this._c = _c;
    this._0._l(true)
};
function _14() {
    this.ver = navigator.appVersion;
    this.agent = navigator.userAgent;
    this.dom = document.getElementById ? 1 : 0;
    this.opera5 = this.agent.indexOf("Opera 5") > -1;
    this.ie5 = this.ver.indexOf("MSIE 5") > -1 && this.dom && !this.opera5;
    this.ie6 = this.ver.indexOf("MSIE 6") > -1 && this.dom && !this.opera5;
    this.ie7 = this.ver.indexOf("MSIE 7") > -1 && this.dom && !this.opera5;
    this.ie8 = this.ver.indexOf("MSIE 8") > -1 && this.dom && !this.opera5;
    this.ie4 = (document.all && !this.dom && !this.opera5) ? 1 : 0;
    this.ie = this.ie4 || this.ie5 || this.ie6 || this.ie7 || this.ie8;
    this.opera7 = ((this.agent.toLowerCase().indexOf('opera 7') > -1) || (this.agent.toLowerCase().indexOf('opera/7') > -1));
    this.opera = window.opera;
    this.operaOld = this.opera && !this.opera7;
    this.realDom = this.dom && !this.operaOld;
    this.ns4 = document.layers && !this.dom && !this.operaOld
}
;
function _m(_A) {
    if (!_m._3) _m._3 = 0;
    this.id = 'do_' + (_m._3++);
    this._A = _A
}
;
$ = _m.prototype;
$._b = function() {
    this.el = bw.dom ? document.getElementById(this.id) : bw.ie4 ? document.all[this.id] : bw.ns4 ? document.layers[this.id] : 0;
    this.css = bw.dom || bw.ie4 ? this.el.style : this.el;
    this.doc = bw.dom || bw.ie4 ? document : this.css.document;
    this.x = parseInt(this.css.left) || this.css.pixelLeft || this.el.offsetLeft || 0;
    this.y = parseInt(this.css.top) || this.css.pixelTop || this.el.offsetTop || 0;
    this.w = this.__();
    this.h = this._G()
};
$._a = function() {
    this.el = null;
    this.css = null;
    this.doc = null
};
$.__ = function() {
    return this.el.offsetWidth || this.css.clip.width || this.doc.width || this.css.pixelWidth || 0
};
$._G = function() {
    return this.el.offsetHeight || this.css.clip.height || this.doc.height || this.css.pixelHeight || 0
};
$._i = function(_x, _y) {
    this.x = _x;
    this.y = _y;
    if (this.el) {
        var px = bw.ns4 || bw.operaOld ? 0 : 'px';
        this.css.left = _x + px;
        this.css.top = _y + px
    }
};
$._19 = function(_h) {
    this.h = _h;
    if (this.el) {
        if (bw.ns4) this.el.resize(this.w, _h);
        else {
            var px = bw.operaOld ? 0 : 'px';
            this.css.height = _h + px
        }
    }
};
$._f = function() {
    if (bw.realDom && !this.el && !this._A) {
        this.el = document.createElement('DIV');
        this.el.innerHTML = this._w;
        this.el.style.position = 'absolute';
        this.el.style.width = this._v || (this.w + 'px');
        this.el.style.left = this.x + 'px';
        this.el.style.top = this.y + 'px';
        this.el.style.zIndex = this._z;
        document.body.appendChild(this.el, 'beforeEnd');
        this.css = this.el.style;
        this.w = this.__();
        this.h = this._G()
    }
    ;
    this.css.visibility = bw.ns4 ? 'show' : "visible"
};
$._e = function() {
    this.css.visibility = bw.ns4 ? 'hide' : "hidden";
    if (bw.realDom && this.el && !this._A) {
        this.el.parentNode.removeChild(this.el);
        this.el.innerHTML = '';
        this.css = null;
        this.el = null
    }
};
$._M = function(w, h, z, _Q) {
    this._w = _Q;
    this._z = z;
    this._v = w;
    window.$bar._16('<div ondrag="return false" id="' + this.id + '" style="position:absolute; z-index:' + z + ';left: 0; top: 0;' + (w ? ' width: ' + w + '; ' : '') + 'height: auto; visibility:hidden;">' + _Q + '</div>')
}