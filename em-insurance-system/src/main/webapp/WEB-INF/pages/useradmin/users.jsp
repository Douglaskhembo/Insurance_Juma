<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script type="text/javascript" src="<c:url value="/js/modules/utils/select2builder.js"/>"></script>

<div class="x_panel">
    <sec:authorize access="hasAnyAuthority('CREATE_USER')">
        <button class="btn btn-success btn btn-info pull-right" id="btn-add-user">New</button>
    </sec:authorize>
    <div class="x_title">
        <h4>Users</h4>
    </div>
        <div class="cutom-container">
            <div class="form-group" align="right">
                    <label >
                        <input type="checkbox" id="show-inactive">
                    Show Inactive users</label>
            </div>
            <input type="hidden" id="status-to-show"/>
    <table id="users-tbl" class="table table-hover table-bordered">

        <thead>
        <tr>

            <th>Name</th>
            <th>Username</th>
            <th>Email</th>
            <th>Status</th>
            <th>User Type</th>
            <th width="5%"></th>
            <th width="5%"></th>
        </tr>
        </thead>
    </table>
            </div>
    <div class=x_content">
        <div class="" role="tabpanel" data-example-id="togglable-tabs">
            <ul id="myTab-2" class="nav nav-tabs bar_tabs" role="tablist">
                <li role="presentation" class="active"><a href="#tab_content1-1"
                                                          id="home-tab-2" role="tab" data-toggle="tab" aria-expanded="true">User Roles</a>
                </li>
                <li role="presentation" class=""><a href="#tab_content2-2"
                                                    role="tab" id="profile-tab-2" data-toggle="tab" aria-expanded="false">User Branches</a>
                </li>
            </ul>
            <div id="myTabContent-1" class="tab-content">
                <div role="tabpanel" class="tab-pane fade active in"
                     id="tab_content1-1" aria-labelledby="home-tab-1">
                    <div class="row">
                        <div class="col-md-1">
                        </div>
                        <div class="col-md-4">
                            <table id="unassigned-roles-tbl" class="table table-hover table-bordered">
                                <thead>
                                <tr>
                                    <th>Role</th>
                                </tr>
                                </thead>
                            </table>
                        </div>
                        <div class="col-md-1 text-center">
                            <p></p>
                            <br>
                            <p></p>
                            <button class="btn btn-success btn btn-info pull-right" id="btn-add-roles">&gt;&gt;</button>
                            <button class="btn btn-success btn btn-info pull-right" id="btn-remove-roles">&lt;&lt;</button>
                            <form id="roles-form">
                                <input type="hidden" name="userId" id="user-id-pk">
                            </form>
                        </div>
                        <div class="col-md-4">
                            <table id="assigned-roles-tbl" class="table table-hover table-bordered">
                                <thead>
                                <tr>
                                    <th>Role</th>
                                </tr>
                                </thead>
                            </table>
                        </div>
                        <div class="col-md-2">
                        </div>
                    </div>
                </div>
                <div role="tabpanel" class="tab-pane fade"
                     id="tab_content2-2" aria-labelledby="home-tab-2">
                    <sec:authorize access="hasAnyAuthority('CREATE_USER')">
                        <button class="btn btn-success btn btn-info pull-right" id="btn-add-branch">New</button>
                    </sec:authorize>
                    <hr>
                    <table id="user_branches_tbl" class="table table-hover table-bordered">
                        <thead>
                        <tr>
                            <th>Branch Assigned</th>
                            <th>Assigned By</th>
                            <th>Assigned Date</th>
                            <th width="5%"></th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="userBranchesModal" tabindex="-1" role="dialog" aria-labelledby="userBranchesModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="userBranchesModalLabel">Select Branches</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="brn-name-search" class="col-md-3 control-label">Branch</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="brn-name-search"
                            >
                        </div>
                        <div class="col-md-1">
                            <button  id="searchBranches"
                                     type="button" class="btn btn-primary">
                                Search
                            </button>
                        </div>
                    </div>
                </form>
                <div style="height: 300px !important; overflow: scroll;">
                    <table class="table table-striped table-hover table-bordered table-fixed" id="userBranchesTbl">
                        <thead>
                        <tr>
                            <th width="1%"></th>
                            <th width="12%">Branch</th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
                <form id="user-branches-form">
                    <input type="hidden" id="brn-user-id" name="userId"/>
                </form>
            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveBranchesBtn"
                        type="button" class="btn btn-success">Save</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Cancel</button>
            </div>
        </div>
    </div>
</div>



<div class="modal fade" id="usersModal" tabindex="-1" role="dialog"
     aria-labelledby="usersModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="usersModalLabel">
                    Edit/Add System User
                </h4>
            </div>
            <form id="users-form" class="form-horizontal">
            <div class="modal-body">
                    <input type="hidden" class="form-control" id="user-id" name="id">
                    <div class="form-group">
                        <label for="user-name" class="col-md-3 control-label">Name</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="user-name"
                                   name="name"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="usr-username" class="col-md-3 control-label">Username</label>
                        <div class="col-md-8">
                            <input type="text" class="form-control" id="usr-username"
                                   name="username"  required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="chk-marketer" class="col-md-3 control-label">Marketer?</label>
                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="checkbox" name="marketer" id="chk-marketer">
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="sub-account-frm" class="col-md-3 control-label">Marketer Type</label>
                        <div class="col-md-8">
                            <input type="hidden" id="sub-id" name="acctTypeId"/>
                            <input type="hidden" id="sub-acct-id" name="acctId"/>
                            <input type="hidden" id="sub-name">
                            <div id="sub-account-frm" class="form-control"
                                 select2-url="<c:url value="/protected/users/subaccounts"/>" >

                            </div>
                        </div>
                    </div>
                    <div class="form-group password">
                        <label for="usr-password" class="col-md-3 control-label">Password</label>
                        <div class="col-md-8">
                            <input type="password" class="form-control" id="usr-password"
                                   name="password">
                        </div>
                    </div>
                    <div class="form-group password">
                        <label for="con-usr-password" class="col-md-3 control-label">Confirm Password</label>
                        <div class="col-md-8">
                            <input type="password" class="form-control" id="con-usr-password"
                                   name="confirmPassword">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="usr-email" class="col-md-3 control-label">Email</label>

                        <div class="col-md-8">
                            <input type="email" class="form-control" id="usr-email"
                                   name="email">
                        </div>
                    </div>
                    <div class="form-group reset-pass">
                        <label for="chk-reset-pass" class="col-md-3 control-label">Reset Password?</label>
                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="checkbox" name="resetPass" id="chk-reset-pass">
                            </label>
                        </div>
                    </div>
                    <div class="form-group reset-pass">
                        <label for="chk-reset-pass" class="col-md-3 control-label">Send Reset Email?</label>
                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="checkbox" name="sendEmail" id="chk-send-email">
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="chk-active" class="col-md-3 control-label">Active?</label>
                        <div class="col-md-8 checkbox">
                            <label>
                                <input type="checkbox" name="status" id="chk-active">
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="avatar" class="col-md-3 control-label">Signature</label>
                        <div class="col-md-9">
                            <div class="kv-avatar" style="width:200px">
                                <input  name="file" type="file" id="avatar" class="file-loading"/>
                            </div>
                        </div>
                    </div>

            </div>
            <div class="modal-footer">
                <button data-loading-text="Saving..." id="saveUsers"
                        type="submit" class="btn btn-success">
                    Save
                </button>
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    Cancel
                </button>
            </div>
            </form>
        </div>
    </div>
</div>

<script>
    $(function(){

        $(document).ready(function() {
            UserModule.init();
            $(document).ajaxStart(function () {
                $("#saveUsers,#saveBranchesBtn,#btn-add-roles,#btn-remove-roles").attr("disabled", true);
            });
            $(document).ajaxComplete(function () {
                $("#saveUsers,#saveBranchesBtn,#btn-add-roles,#btn-remove-roles").attr("disabled", false);
            });
        });

    });

    var UserModule = (function($){

        function unassignedRoles(userId) {
            var url = "unAssignedRoles/"+userId;
            var table = $('#unassigned-roles-tbl').DataTable({
                "processing": true,
                "serverSide": true,
                "sDom": 't',
                "ajax": url,
                lengthMenu: [[200], [200]],
                pageLength: 200,
                destroy: true,
                "columns": [
                    {"data": "roleName"},
                ],
                initComplete: function () {
                    $("#unassigned-roles-tbl").enableExtendedSelection();
                }
            });

            $("#unassigned-roles-tbl").selectable(true);
            //$('#unassigned-roles-tbl tbody').on( 'click', 'tr', function () {
            //    $(this).toggleClass('selected');
            //} );
        }

        function assignedRoles(userId) {
            var url = "assignedRoles/"+userId;
            var table = $('#assigned-roles-tbl').DataTable({
                "processing": true,
                "serverSide": true,
                "sDom": 't',
                "ajax": url,
                lengthMenu: [[200], [200]],
                pageLength: 200,
                destroy: true,
                "columns": [
                    {"data": "roleName"},
                ],
                initComplete: function () {
                    // enables multi selection extension
                    $("#assigned-roles-tbl").enableExtendedSelection();
                }
            });

            $("#assigned-roles-tbl").selectable(true);
        }


        function getUserAssignedBranches(userId){
            var url = "getUserAssignedBranches/"+userId;
            return $('#user_branches_tbl').DataTable({
                "processing": true,
                "serverSide": true,
                autoWidth: true,
                "ajax": {
                    'url': url,
                },
                lengthMenu: [[10, 15, 20], [10, 15, 20]],
                pageLength: 10,
                destroy: false,
                "columns": [
                    {"data": "branchName"},
                    {"data": "userAssigned"},
                    {"data": "dateAssigned"},
                    {
                        "data": "userBranchId",
                        "render": function (data, type, full, meta) {
                            return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-userbranches=' + encodeURI(JSON.stringify(full)) + ' onclick="UserModule.confirmDelUserBranch(this);"><i class="fa fa-pencil-square-o"></button>';

                        }

                    }
                ]
            });
        }




        function newUser(){
            $("#btn-add-user").on("click", function(){
                $('#users-form').find("input[type=text],input[type=mobileNumber],input[type=emailFull],input[type=password],input[type=hidden],input[type=number],input[type=email], textarea,select").val("");
                $(".reset-pass").css("display","none");
                $("#chk-reset-pass").prop("checked", false);
                $("#chk-active").prop("checked", false);
                $("#chk-marketer").prop("checked", false);
                $(".password").show();
                populateSubAccountLov();
                $("#sub-account-frm").select2("enable", false);
                $('#usersModal').modal('show');
            });
        }

        var populateSubAccountLov = function(){
            if($("#sub-account-frm").filter("div").html() != undefined)
            {
                Select2Builder.initAjaxSelect2({
                    containerId : "sub-account-frm",
                    sort : 'accName',
                    change: function(e, a, v){
                        $("#sub-id").val(e.added.accId);

                    },
                    formatResult : function(a)
                    {
                        return a.accName;
                    },
                    formatSelection : function(a)
                    {
                        return a.accName;
                    },
                    initSelection: function (element, callback) {
                        var code  = $('#sub-id').val();
                        var name = $("#sub-name").val();
                        var data = {accName:name,accId:code};
                        callback(data);
                    },
                    id: "accId",
                    width:"250px",
                    placeholder:"Select Marketer Type"

                });
            }
        };


        function saveUser(){
            $('form#users-form')
                .submit( function( e ) {
                    var $form = $("#users-form");
                    var validator = $form.validate();
                    e.preventDefault();
                    if (!$form.valid()) {
                        return;
                    }
                    var data = new FormData( this );
                    data.append( 'file', $( '#avatar' )[0].files[0] );
                    $.ajax( {
                        url: 'createUsers',
                        type: 'POST',
                        data: data,
                        processData: false,
                        contentType: false,
                        success: function (s ) {
                            new PNotify({
                                title: 'Success',
                                text: 'Record created/updated Successfully',
                                type: 'success',
                                styling: 'bootstrap3'
                            });
                            $('#users-tbl').DataTable().ajax.reload();
                            $('#unassigned-roles-tbl').DataTable().destroy();
                            $('#unassigned-roles-tbl tbody').empty();
                            $('#assigned-roles-tbl').DataTable().destroy();
                            $('#assigned-roles-tbl tbody').empty();
                            $(".userroles").hide();
                            validator.resetForm();
                            $('#users-form').find("input[type=text],image,input[type=hidden],input[type=mobileNumber],input[file],input[type=email],input[type=password],input[type=hidden],input[type=number], textarea,select").val("");
                            $("#avatar").val('');
                            $('#usersModal').modal('hide');

                        },
                        error: function(jqXHR, textStatus, errorThrown){
                            new PNotify({
                                title: 'Error',
                                text: jqXHR.responseText,
                                type: 'error',
                                styling: 'bootstrap3'
                            });
                        }
                    });
                });
        }


        function createUsers(userstatus){
            var url = "usersList/"+userstatus;
            var table = $('#users-tbl').DataTable( {
                "processing": true,
                "serverSide": true,
                "ajax": url,
                lengthMenu: [ [10, 15], [10, 15] ],
                pageLength: 10,
                destroy: true,
                "columns": [
                    { "data": "name" },
                    { "data": "username" },
                    { "data": "email" },
                    { "data": "status",
                        "render": function ( data, type, full, meta ) {
                           if(full.status && full.status==='1'){
                               return 'Active';
                           }
                           else{
                               return 'Inactive';
                           }
                        }
                    },
                    { "data": "accType" },
                    {
                        "data": "id",
                        "render": function ( data, type, full, meta ) {
                            return '<button type="button" class="btn btn-success btn btn-info btn-xs" data-users='+encodeURI(JSON.stringify(full)) + ' onclick="UserModule.editUser(this);"><i class="fa fa-pencil-square-o"></button>';
                        }

                    },
                    {
                        "data": "id",
                        "render": function ( data, type, full, meta ) {
                            return '<button type="button" class="btn btn-danger btn btn-danger btn-xs" data-users='+encodeURI(JSON.stringify(full)) + ' onclick="UserModule.confirmDelUser(this);"><i class="fa fa-trash-o"></button>';
                        }

                    },
                ]
            } );

            $('#users-tbl tbody').on( 'click', 'tr', function () {
                $(this).addClass('active').siblings().removeClass('active');
                var aData = table.rows('.active').data();
                if (aData[0] === undefined || aData[0] === null) {
                }
                else{
                    $(".userroles").show();
                    $("#user-id-pk").val(aData[0].id);
                    assignedRoles(aData[0].id);
                    unassignedRoles(aData[0].id);
                    $('#user_branches_tbl').DataTable().ajax.url( "getUserAssignedBranches/"+aData[0].id ).load();
                }
            } );

            return table;
        }

        function editUser(button){
            var users = JSON.parse(decodeURI($(button).data("users")));
            $("#user-id").val(users["id"]);
            $("#user-name").val(users["name"]);
            $("#usr-username").val(users["username"]);
            $("#usr-email").val(users["email"]);
            if(users["status"]==="1"){
                $("#chk-active").prop("checked", true);
            }else {
                $("#chk-active").prop("checked", false);
            }
            $("#chk-reset-pass").prop("checked", false);
            if(users["acctId"]){
                $("#sub-account-frm").select2("enable", true);
                $("#chk-marketer").prop("checked", true);
                $("#sub-id").val(users["acctTypeId"]);
                $("#sub-acct-id").val(users["acctId"]);
                $("#sub-name").val(users["accountType"]);
            }else {
                $("#sub-account-frm").select2("enable", false);
                $("#chk-marketer").prop("checked", false);
            }
            $(".reset-pass").css("display","block");
            $(".password").hide();
            getUserSignature(users["id"]);
            populateSubAccountLov();
            $('#usersModal').modal('show');
        }

        function getUserSignature(userId){
            console.log('user id',userId);
            $("#avatar").val('');
            $("#avatar").fileinput('refresh',{
                overwriteInitial: false,
                maxFileSize: 1500,
                showClose: false,
                showCaption: false,
                browseLabel: '',
                removeLabel: '',
                browseIcon: '<i class="fa fa-folder-open"></i>',
                removeIcon: '<i class="fa fa-times"></i>',
                removeTitle: 'Cancel or reset changes',
                elErrorContainer: '#kv-avatar-errors',
                msgErrorClass: 'alert alert-block alert-danger',
                defaultPreviewContent: '<img src="'+getContextPath()+'/protected/users/usersignature/'+userId+'"  style="width:40px">',
                layoutTemplates: {main2: '{preview} ' + ' {remove} {browse}'},
                allowedFileExtensions: ["jpg", "png", "gif"]
            });
        }

        function getContextPath() {
            return window.location.pathname.substring(0, window.location.pathname
                .indexOf("/", 2));
        }

        function confirmDelUser(button){
            var users = JSON.parse(decodeURI($(button).data("users")));
            bootbox.confirm("Are you sure want to deactivate "+users["name"]+"?", function(result) {
                if(result){
                    $.ajax({
                        type: 'GET',
                        url:  'deleteUser/' + users["id"],
                        dataType: 'json',
                        async: true,
                        success: function(result) {
                            new PNotify({
                                title: 'Success',
                                text: 'Record Deleted Successfully',
                                type: 'success',
                                styling: 'bootstrap3'
                            });
                            $('#users-tbl').DataTable().ajax.reload();
                            $(".userroles").hide();
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            new PNotify({
                                title: 'Error',
                                text: jqXHR.responseText,
                                type: 'error',
                                styling: 'bootstrap3'
                            });
                        }
                    });
                }

            });
        }


        function searchUserBranches(search){
            if($("#user-id-pk").val() !== ''){
                $.ajax({
                    type: 'GET',
                    url:  'userbranches',
                    dataType: 'json',
                    data: {"userId": $("#user-id-pk").val(),"search":search},
                    async: true,
                    success: function(result) {
                        $("#userBranchesTbl tbody").each(function(){
                            $(this).remove();
                        });
                        for(var res in result){
                            var markup = "<tr><td><input type='checkbox' name='record' id='"+result[res].obId+"'></td><td>" + result[res].obName + "</td></tr>";
                            $("#userBranchesTbl").append(markup);
                        }
                        $("#brn-user-id").val($("#user-id-pk").val());
                        $('#userBranchesModal').modal({
                            backdrop: 'static',
                            keyboard: true
                        })
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        new PNotify({
                            title: 'Error',
                            text: jqXHR.responseText,
                            type: 'error',
                            styling: 'bootstrap3'
                        });
                    }
                });
            }
            else{
                bootbox.alert("Select User to attach Branches")
            }
        }


        function saveUserBranches(){
            var arr = [];
            $("#saveBranchesBtn").click(function(){
                $("#userBranchesTbl tbody").find('input[name="record"]').each(function(){
                    if($(this).is(":checked")){
                        arr.push($(this).attr("id"));
                    }
                });
                if(arr.length===0){
                    bootbox.alert("No Branches Selected to attach..");
                    return;
                }
                var $currForm = $('#user-branches-form');
                if (!$currForm.valid()) {
                    return;
                }
                var data = {};
                $currForm.serializeArray().map(function(x) {
                    data[x.name] = x.value;
                });
                var url = "assignUserBranches";
                data.branches = arr;
                $.ajax({
                    url : url,
                    type : "POST",
                    data : JSON.stringify(data),
                    success : function(s) {
                        new PNotify({
                            title: 'Success',
                            text: 'Records Created Successfully',
                            type: 'success',
                            styling: 'bootstrap3'
                        });
                        $('#user_branches_tbl').DataTable().ajax.reload();
                        $('#userBranchesModal').modal('hide');
                        arr = [];
                    },
                    error : function(jqXHR, textStatus, errorThrown) {
                        new PNotify({
                            title: 'Error',
                            text: jqXHR.responseText,
                            type: 'error',
                            styling: 'bootstrap3'
                        });
                    },
                    dataType : "json",
                    contentType : "application/json"
                });
            })
        }

        function confirmDelUserBranch(button){
            var branches = JSON.parse(decodeURI($(button).data("userbranches")));
            bootbox.confirm("Are you sure want to delete "+branches["branchName"]+"?", function(result) {
                if(result){
                    $.ajax({
                        type: 'GET',
                        url:  'deleteUserBranch/' + branches["userBranchId"],
                        dataType: 'json',
                        async: true,
                        success: function(result) {
                            new PNotify({
                                title: 'Success',
                                text: 'Record Deleted Successfully',
                                type: 'success',
                                styling: 'bootstrap3'
                            });
                            $('#user_branches_tbl').DataTable().ajax.reload();
                        },
                        error: function(jqXHR, textStatus, errorThrown) {
                            new PNotify({
                                title: 'Error',
                                text: jqXHR.responseText,
                                type: 'error',
                                styling: 'bootstrap3'
                            });
                        }
                    });
                }

            });
        }

        function init(){
            createUsers("1");
            newUser();
            saveUser();
            saveUserBranches();
            getUserAssignedBranches(-2000);
            $(".userroles").hide();

            $("#searchBranches").click(function(){
                searchUserBranches($("#brn-name-search").val());
            });

            $("#btn-add-branch").click(function(){
                searchUserBranches("");
            });

            $('#show-inactive').click(function(){
                if($(this).is(':checked')){
                    createUsers("0");
                } else {
                    createUsers("1");
                }
            });

            $('#chk-marketer').click(function() {
                if($(this).is(':checked')){
                    $("#sub-account-frm").select2("enable", true);
                }
                else $("#sub-account-frm").select2("enable", false);
            });


            $('#btn-add-roles').click( function () {
                var arr = $("#unassigned-roles-tbl").data().selection.items;
                var myArray = [];
                for (var j = 0; j < arr.length; j++){
                    myArray.push(arr[j].roleId);
                }

                if(myArray.length==0){
                    bootbox.alert("No Roles Selected to assign..");
                    return;
                }

                var $currForm = $('#roles-form');
                var currValidator = $currForm.validate();
                if (!$currForm.valid()) {
                    return;
                }

                var data = {};
                $currForm.serializeArray().map(function(x) {
                    data[x.name] = x.value;
                });
                var url = "assignRoles";
                data.roles = myArray;
                $.ajax({
                    url : url,
                    type : "POST",
                    data : JSON.stringify(data),
                    async: true,
                    success : function(s) {
                        new PNotify({
                            title: 'Success',
                            text: 'Roles(s) assigned successfully',
                            type: 'success',
                            styling: 'bootstrap3'
                        });
                        $('#unassigned-roles-tbl').DataTable().ajax.reload();
                        $('#assigned-roles-tbl').DataTable().ajax.reload();
                    },
                    error : function(jqXHR, textStatus, errorThrown) {
                        console.log(jqXHR);
                        new PNotify({
                            title: 'Error',
                            text: jqXHR.responseText,
                            type: 'error',
                            styling: 'bootstrap3'
                        });
                    },
                    dataType : "json",
                    contentType : "application/json"
                });


            } );

            $('#btn-remove-roles').click( function () {
                var arr = $("#assigned-roles-tbl").data().selection.items;
                var myArray = [];
                for (var j = 0; j < arr.length; j++){
                    myArray.push(arr[j].roleId);
                }
                if(myArray.length==0){
                    bootbox.alert("No Roles Selected to Un Assign..");
                    return;
                }

                var $currForm = $('#roles-form');
                var currValidator = $currForm.validate();
                if (!$currForm.valid()) {
                    return;
                }

                var data = {};
                $currForm.serializeArray().map(function(x) {
                    data[x.name] = x.value;
                });
                var url = "unAssignRoles";
                data.roles = myArray;
                $.ajax({
                    url : url,
                    type : "POST",
                    async: true,
                    data : JSON.stringify(data),
                    success : function(s) {
                        new PNotify({
                            title: 'Success',
                            text: 'Roles(s) Un assigned successfully',
                            type: 'success',
                            styling: 'bootstrap3'
                        });
                        $('#unassigned-roles-tbl').DataTable().ajax.reload();
                        $('#assigned-roles-tbl').DataTable().ajax.reload();
                    },
                    error : function(jqXHR, textStatus, errorThrown) {
                        new PNotify({
                            title: 'Error',
                            text: jqXHR.responseText,
                            type: 'error',
                            styling: 'bootstrap3'
                        });
                    },
                    dataType : "json",
                    contentType : "application/json"
                });

            });
        }

        return {
            editUser:editUser,
            confirmDelUser:confirmDelUser,
            confirmDelUserBranch:confirmDelUserBranch,
            init:init
        }

    })(jQuery);

</script>