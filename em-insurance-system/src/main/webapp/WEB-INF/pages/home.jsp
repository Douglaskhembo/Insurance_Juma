<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<script type="text/javascript"
        src="<c:url value="/js/modules/home.js"/>"></script>

<div class="row tile_count">
  <div class="col-md-4 col-sm-4 col-xs-6 tile_stats_count">
    <span class="count_top"><i class="fa fa-money"></i>Sum Assured YTD</span>
    <div class="count blue pending-quote">0.00</div>
    <span class="count_bottom"><i class="blue"></i></span>
    </div>
  <div class="col-md-4 col-sm-4 col-xs-6 tile_stats_count">
    <span class="count_top"><i class="fa fa-money"></i>Policies Sold YTD</span>
    <div class="count blue expired-pols">0</div>
    <span class="count_bottom"><i class="blue"></i></span>
  </div>
  <div class="col-md-4 col-sm-4 col-xs-6 tile_stats_count">
    <span class="count_top"><i class="fa money"></i> Total Premium YTD</span>
    <div class="count blue pend-endorse">0.00</div>
    <span class="count_bottom"><i class="blue"></i></span>
  </div>
<%--  <a href="<c:url value="/protected/home/pendingrenewals"/>">--%>
<%--  <div class="col-md-3 col-sm-4 col-xs-6 tile_stats_count">--%>
<%--    <span class="count_top"><i class="fa fa-user"></i>Pending Renewals</span>--%>
<%--    <div class="count blue pend-ren">0</div>--%>
<%--    <span class="count_bottom"><i class="blue"></i></span>--%>
<%--  </div>--%>
<%--  </a>--%>
</div>

<div class="row">
  <div class="col-md-12 col-sm-12 col-xs-12">
    <div class="" role="tabpanel" data-example-id="togglable-tabs">
      <ul id="myTab" class="nav nav-tabs bar_tabs" role="tablist">
        <li role="presentation" class="active"><a href="#tab_content3"
                                                  role="tab" id="task-tab" data-toggle="tab" aria-expanded="false">Pending Transactions</a>
        </li>
        <li role="presentation" class=""><a href="#tab_content1"
                                            id="home-tab" role="tab" data-toggle="tab" aria-expanded="true">Dashboard</a>
        </li>
<%--        <li role="presentation" class=""><a href="#tab_content2"--%>
<%--                                            role="tab" id="calendar-tab" data-toggle="tab" aria-expanded="false">Calendar</a>--%>
<%--        </li>--%>

      </ul>
      <div id="myTabContent" class="tab-content">
        <div role="tabpanel" class="tab-pane fade active in"
             id="tab_content3" aria-labelledby="task-tab">
          <div class="x_panel">

            <table id="pol_tbl" class="table table-striped table-bordered">
              <thead>
              <tr>
                <th>Task ID</th>
                <th>Task Name</th>
                <th>Ref No</th>
                <th>Client</th>
                <th>Prep. By</th>
                <th>Created</th>
                <th width="5%"></th>
              </tr>
              </thead>
            </table>
          </div>
        </div>
        <div role="tabpanel" class="tab-pane fade"
             id="tab_content1" aria-labelledby="home-tab">
          <div class="dashboard_graph">

            <div class="row x_title">
              <div class="col-md-6">
                <h3>Premium Production</h3>
              </div>
              <div class="col-md-6">

              </div>
            </div>

            <div class="col-md-12 col-sm-12 col-xs-12">
              <canvas id="lineChart" class="demo-placeholder"></canvas>
            </div>

            <div class="row">


              <div class="col-md-4 col-sm-4 col-xs-12">
                <div class="x_panel tile fixed_height_350">
                  <div class="x_title">
                    <h2>Top 5 Products</h2>
                    <div class="clearfix"></div>
                  </div>
                  <div class="x_content">
                    <canvas id="productsDoughnut" height="2" width="2"></canvas>
                  </div>
                </div>
              </div>

              <div class="col-md-4 col-sm-4 col-xs-12">
                <div class="x_panel tile fixed_height_350">
                  <div class="x_title">
                    <h2>Top 5 Branches</h2>
                    <div class="clearfix"></div>
                  </div>
                  <div class="x_content">
                    <canvas id="branchesDoughnut" height="2" width="2"></canvas>
                  </div>
                </div>
              </div>

            </div>


            <div class="clearfix"></div>
          </div>
        </div>
<%--        <div role="tabpanel" class="tab-pane fade"--%>
<%--             id="tab_content2" aria-labelledby="calendar-tab">--%>
<%--          <div id='calendar'></div>--%>

<%--        </div>--%>
      </div>
    </div>
  </div>

</div>
<br />




