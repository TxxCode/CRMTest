<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bs_pagination/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		//时间控件拾取器
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

	    //为创建按钮绑定事件，打开添加操作的模态窗口
        $("#addBtn").click(function () {

            //绑定下拉框用户
            $.ajax({
                url:"workbench/activity/getUserList.do",
                type: "Get",
                dataType:"json",
                success:function (data) {

                    var html = "<option></option>";

                    //遍历出来的每一个n，就是一个user用户
                    $.each(data,function (i,n) {
                        html +="<option value='"+n.id+"'>"+n.name+"</option>"
                    })

                    $("#create-owner").html(html);
                    //当前登录的用户，设置为下拉框默认的选项
					var id = "${sessionScope.user.id}"
					$("#create-owner").val(id);
                }
            })
            //需要操作的模态窗口query对象，调用modal方法，show打开，hide模糊窗口
            $("#createActivityModal").modal("show");
        })
		//为按钮绑定事件，添加操作
		$("#saveBtn").click(function () {

			//绑定下拉框用户
			$.ajax({
				url: "workbench/activity/save.do",
				data:{
					"owner":$.trim($("#create-owner").val()),
					"name":$.trim($("#create-name").val()),
					"startDate":$.trim($("#create-startDate").val()),
					"endDate":$.trim($("#create-endDate").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-description").val())
				},
				type: "post",
				dataType:"json",
				success: function (data) {
					if(data.success){
						/*添加成功
					   添加成功后刷新活动信息表，局部刷新
						pageList(1,2); 不行，用户体验不好

						操作后停留在当前页
						$("#activityPage").bs_pagination('getOption', 'currentPage')
						操作后维持已经设置好的每页显示多少条数据
						$("#activityPage").bs_pagination('getOption', 'rowsPerPage')
					 */

					    pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

/*
					    清空添加操作模态窗口的数据
						提交表单
							注意：我们拿到了form表单的jQuery对象
							对于表单jquery对象，提供了submit()方法让我们提交表单
							但jQuery没有提供reset方法，但是原生的js提供了reset方法
							所以我们要将jquery对象转化为原生dom对象

							jquery对象转化为dom对象:    jquery对象[下标]
							dom对象转化为jquery对象:     $(dom)
*/
						$("#activityAddForm")[0].reset();

						//隐藏添加模态框
						$("#createActivityModal").modal("hide");
						//alert("添加成功");

					}else{
						alert('添加市场活动失败');

					}
				}
			})
		})

		//为修改按钮绑定事件，打开修改操作的模态窗口
		$("#editBtn").click(function () {
			var $xz = $("input[name=xz]:checked");

			if($xz.length==0){
				alert('请选择需要修改的活动');
			}else if($xz.length>1){
				alert('只能选择一条修改');
			}else {
				var id = $xz.val();

				//修改数据填充
				$.ajax({
					url: "workbench/activity/getUserListAndActivity.do",
					data: {
						"id":id
					},
					type: "get",
					dataType: "json",
					success: function (data) {

						//处理所有者的下拉框
						var html = "<option></option>";
						$.each(data.uList,function (i,n) {
							html += "<option value='"+n.id+"'>"+n.name+"</option>";
						})
						$("#edit-owner").html(html);

						//处理单条ativity
						$("#edit-id").val(data.a[0].id);
						$("#edit-name").val(data.a[0].name);
						$("#edit-owner").val(data.a[0].owner);
						$("#edit-startDate").val(data.a[0].startDate);
						$("#edit-endDate").val(data.a[0].endDate);
						$("#edit-cost").val(data.a[0].cost);
						$("#edit-description").val(data.a[0].description);

						//所有值填写完毕后，打开修改操作的模态窗口
						$("#editActivityModal").modal("show");

					}
				})
			}
		})
		//为更新按钮绑定事件，修改操作
		$("#updateBtn").click(function () {

			$.ajax({
				url: "workbench/activity/update.do",
				data:{
					"id":$.trim($("#edit-id").val()),
					"owner":$.trim($("#edit-owner").val()),
					"name":$.trim($("#edit-name").val()),
					"startDate":$.trim($("#edit-startDate").val()),
					"endDate":$.trim($("#edit-endDate").val()),
					"cost":$.trim($("#edit-cost").val()),
					"description":$.trim($("#edit-description").val())
				},
				type: "post",
				dataType:"json",
				success: function (data) {
					if(data.success) {


						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						$("#editActivityModal").modal("hide");
					}else{
						alert("修改失败");
					}
				}
			})
		})

		//弹出删除模态框
		$("#delBtn").click(function () {
			//找到复选框中所调√的复选框jquery对象
			var $xz = $("input[name=xz]:checked");

			if($xz.length==0){
				alert('请选择需要删除的行记录');
			}else {
				$("#delModal").modal("show");
			}
		})
		//为删除按钮绑定事件，执行市场操作删除操作
		$("#deleteBtn").click(function () {

			var $xz = $("input[name=xz]:checked");
			//拼接参数
			var param = "";
			//将$xz中的每一个dom对象遍历出来，取其value值，就相当于取得了需要删除的记录id
			for (var i = 0; i < $xz.length; i++) {

				param += "id=" + $($xz[i]).val();
				//如果不是最后一个元素，需要在后面追加一个&符
				if (i < $xz.length - 1) {
					param += "&";
				}
			}


			$.ajax({
				url: "workbench/activity/delete.do",
				data: param,
				type: "post",
				dataType: "json",
				success: function (data) {
					if(data.success){
						//删除成功后
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

					}else{
						alert("删除失败");
					}
				}
			})
			$("#delModal").modal("hide");
		})

		//为查询按钮绑定时间，查询操作
		$("#searchBtn").click(function () {

			/*点击查询按钮的时候，我们应该将搜索框中的信息保存下来*/
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-starDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));

            pageList(1,2);
		})

		//为全选的复选框绑定事件，触发全选操作
		$("#qx").click(function () {
			$("input[name=xz]").prop("checked",this.checked);
		})

	/*	动态生成的元素，不能够以普通的绑定事件的形式来进行操作的
		动态生产的元素，我们要以on方法的形式来触发事件
		语法：
			$(需要绑定元素的有效的外层元素).on(绑定事件的方式，需要绑定的元素jquery对象，回调函数)
			*/
		//当复选框没有选满的时候，上面的全选框需要取消选中
		$("#activityBody").on("click",$("input[name=xz]"),function () {

			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		})


		//页面加载完毕后触发方法
		pageList(1,2);
	});
	/*
	* 我们在哪些需要刷新列表
	* (1)点击市场活动
	* (2)添加，修改，删除后需要刷新列表
	* (3)点击查询的时候
	* (4)分页组件的时候
	* */
	function pageList(pageNo,pageSize) {

		//列表局部刷新后将全选√去掉
		$("#qx").prop("checked",false);

		//查询前，将隐藏域中的保存信息取出来，重新赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-starDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		//绑定下拉框用户
		$.ajax({
			url: "workbench/activity/pageList.do",
			data: {
				"pageNo": pageNo,
				"pageSize": pageSize,
				"name": $.trim($("#search-name").val()),
				"owner": $.trim($("#search-owner").val()),
				"startDate": $.trim($("#search-starDate").val()),
				"endDate": $.trim($("#search-endDate").val())
			},
			type: "get",
			dataType: "json",
			success: function (data) {

				var html = "";
				$.each(data.dataList, function (i, n) {

					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="' + n.id + '" /></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">' + n.name + '</a></td>';
					html += '<td>' + n.owner + '</td>';
					html += '<td>' + n.startDate + '</td>';
					html += '<td>' + n.endDate + '</td>';
					html += '</tr>';
				})
				$("#activityBody").html(html);
                //总页数
                var totalPages = data.total%pageSize==0? data.total/pageSize:parseInt(data.total/pageSize)+1;

                //分页插件
                $("#activityPage").bs_pagination({

                    currentPage: pageNo, // 页码
                    rowsPerPage: pageSize, // 每页显示的记录条数
                    maxRowsPerPage: 20, // 每页最多显示的记录条数
                    totalPages: totalPages, // 总页数
                    totalRows: data.total, // 总记录条数

                    visiblePageLinks: 3, // 显示几个卡片

                    showGoToPage: true,
                    showRowsPerPage: true,
                    showRowsInfo: true,
                    showRowsDefaultInfo: true,

                    //该回调函数，点击分页组件的时候触发
                    onChangePage : function(event, data){
                        pageList(data.currentPage , data.rowsPerPage);
                    }
                });
			}
		})
	}
</script>
</head>
<body>

    <!--存储查询框内容，隐藏域-->
	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startDate">
	<input type="hidden" id="hidden-endDate">

	<!-- 删除确认提示模态窗口 -->
	<div class="modal fade" id="delModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 30%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">提示</h4>
				</div>
				<div class="modal-body">
					<p>您确定要删除选中活动？</p>
				</div>
				<div class="modal-footer">
					<button type="button" id="deleteBtn" class="btn btn-danger" >确定</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<%--
						data-dismiss="modal"
						关闭窗口

					--%>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">

						<input type="hidden" id="edit-id"/>

						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label" >开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate" readonly>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control time" type="text" id="search-starDate" readonly />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control time" type="text" id="search-endDate" readonly />
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
                  <%--data-toggle="modal":
                        表示触发该按钮，将要打开一个模态窗口
                       Data-target="#createActivityModal",
                       表示打开一个莫泰窗口，通过#id的形式找到。

                        所以未来实际项目开发，对于触发模态窗口的操作，一定不要写死在元素当中，
                        应当由我们自己写在js代码来操作
                   --%>
				  <button type="button" id="addBtn" class="btn btn-primary"  data-target="#createActivityModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" id="editBtn" class="btn btn-default"  data-target="#editActivityModal"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" id="delBtn" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input id="qx" type="checkbox" />全选</td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">

					<%--	<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
                <div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>