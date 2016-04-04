<!-- /.row -->
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
               ${entity.label}
            </div>            
            <div class="panel-body" ng-init="get()"   ng-controller="${entity.objectName}FormCtrl">
                <div class="row">
                {{message}}
                </div>
                <div class="row">
                    <div class="col-lg-6">
                        <form role="form"  novalidate >
                            ${FORM_FIELDS}  
                            <button ng-click="save()"  type="submit" class="btn btn-default">Add New</button>
                            <button ng-click="update()"  type="submit" class="btn btn-default">Update</button>
                            <button type="reset" class="btn btn-default">Reset</button>
                            <button ng-click="go('dashboard.${entity.varName}List')" type="submit" class="btn btn-default">Cancel</button>
                        </form>
                    </div>                   
                </div>
                <!-- /.row (nested) -->
            </div>
            <!-- /.panel-body -->
        </div>
        <!-- /.panel -->
    </div>
    <!-- /.col-lg-12 -->
</div>
