layui.define(['layer'], function(exports) {
	"use strict";

	var $ = layui.jquery,
		layer = layui.layer;

	var tools = {
        /**
		 * 带有选择框的表格绑定多选事件
         * @param tblId
         */
		tableBindCheckClick: function (tblId) {
			var tblObj = $("#" + tblId);

            tblObj.on('click', 'thead input[type="checkbox"]', function () {
                var obj = $("#" + tblId + " tbody input[type='checkbox']:checkbox");
                if (this.checked) {
                    obj.prop("checked", true);
                } else {
                    obj.prop("checked", false);
                }
            });

        },
        /**
		 * 获取带有选择框的表格选中的记录Ids
         * @param tblId
         * @returns {string}
         */
		getTableCheckedRowIds: function (tblId) {
            var ids = '';
            var tblObj = $("#" + tblId);

            tblObj.find(" tbody input[type=checkbox]:checked").each(function(){
                ids += $(this).val() + ',';
            });

            // 去除最后一位逗号
            ids = ids.substr(0, (ids.length-1));
            return ids;
        }


	};

	exports('tools', tools);
});