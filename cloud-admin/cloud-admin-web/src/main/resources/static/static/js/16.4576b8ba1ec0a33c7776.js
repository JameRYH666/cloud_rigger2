webpackJsonp([16],{"2juu":function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var s=n("NRBp"),i=n("xR4V"),a={components:{Slide:s.a},data:function(){return{menuList:[],isCollapse:!0}},mounted:function(){this.getSlide()},methods:{getSlide:function(){var e=this;Object(i.e)({menuId:2,menuName:"通知公告",sysCode:"JEE_ADMIN_SYSTEM"}).then(function(t){var n=t.value;n&&"0000"==n.code&&(e.menuList=n.data)})},goHome:function(){}}},o={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("el-container",{staticClass:"container-main"},[n("el-aside",{staticClass:"leftMenu",attrs:{width:"auto"}},[n("div",{staticClass:"navLeader"},[e.isCollapse?n("h3",[e._v("功能菜单")]):e._e(),e._v(" "),n("p",{on:{click:e.goHome}},[n("span",{staticClass:"icon iconfont el-icon-extend-shouye1"})])]),e._v(" "),n("Slide",{attrs:{menuList:e.menuList,isCollapse:e.isCollapse}})],1),e._v(" "),n("el-main",[n("router-view")],1)],1)},staticRenderFns:[]};var l=n("VU/8")(a,o,!1,function(e){n("x9V/")},null,null);t.default=l.exports},"x9V/":function(e,t){}});
//# sourceMappingURL=16.4576b8ba1ec0a33c7776.js.map