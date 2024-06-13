/**
 * Created by peter on 2/22/2017.
 */

var HomeScreen = (function($){
    'use strict';
    var getUserTickets = function(){
        var url = SERVLET_CONTEXT + '/protected/home/userTickets';
        var currTable = $('#pol_tbl').DataTable(UTILITIES.extendsOpts({
            "ajaxUrl":url,
            "order": [[ 5, "desc" ]],
            "columns": [
                { "data": "taskId" },
                { "data": "activeProcess" },
                { "data": "refNo" },
                { "data": "clientName" },
                { "data": "username" },
                { "data": "taskId",
                    "render": function ( data, type, full, meta ) {
                        if(full.createdDate) {
                            return timeago().format(full.createdDate);
                        }
                        else{
                            return "";
                        }
                    }
                },
                {
                    "data": "taskId",
                    "render": function ( data, type, full, meta ) {
                         if(full.transType ==='P')
                            return '<form action="edituwtrans" method="post"><input type="hidden" name="id" value=' + full.transactionId + '><input type="submit"  class="btn btn-success btn btn-info btn-xs" value="Edit" ></form>';
                         else if(full.transType ==='Q')
                             return '<form action="editquottrans" method="post"><input type="hidden" name="id" value=' + full.transactionId + '><input type="submit"  class="btn btn-success btn btn-info btn-xs" value="Edit" ></form>';

                    }

                },
            ]
        } ));
        return currTable;
    };

    var updateBranchesDoughnut = function(){
        if ($('#branchesDoughnut').length ){

            $.ajax( {
                url: 'home/branchPremium',
                type: 'GET',
                processData: false,
                contentType: false,
                success: function (s ) {
                    var branches = [];
                    var production = [];
                    for(var i=0;i < s.length;i++){
                        branches.push(s[i].branch);
                        production.push(s[i].premium);
                    }
                    var ctx = document.getElementById("branchesDoughnut");
                    var data = {
                        labels: branches,
                        datasets: [{
                            data: production,
                            backgroundColor: [
                                "#AF144B",
                                "#9B59B6",
                                "#BDC3C7",
                                "#26B99A",
                                "#3498DB"
                            ],
                            hoverBackgroundColor: [
                                "#AF144B",
                                "#B370CF",
                                "#CFD4D8",
                                "#36CAAB",
                                "#49A9EA"
                            ]

                        }]
                    };

                    var canvasDoughnut = new Chart(ctx, {
                        type: 'doughnut',
                        tooltipFillColor: "rgba(51, 51, 51, 0.55)",
                        data: data
                    });

                },
                error: function(xhr, error){
                    if(xhr)
                        bootbox.alert(xhr.responseText);
                    else console.log(xhr);
                }
            });
        }
    };

    var updateProductsDoughnut = function(){
        if ($('#productsDoughnut').length ){

            $.ajax( {
                url: 'home/productPremium',
                type: 'GET',
                processData: false,
                contentType: false,
                success: function (s ) {
                    var products = [];
                    var production = [];
                    for(var i=0;i < s.length;i++){
                        products.push(s[i].product);
                        production.push(s[i].premium);
                    }
                    var ctx = document.getElementById("productsDoughnut");
                    var data = {
                        labels: products,
                        datasets: [{
                            data: production,
                            backgroundColor: [
                                "#AF144B",
                                "#9B59B6",
                                "#BDC3C7",
                                "#26B99A",
                                "#3498DB"
                            ],
                            hoverBackgroundColor: [
                                "#AF144B",
                                "#B370CF",
                                "#CFD4D8",
                                "#36CAAB",
                                "#49A9EA"
                            ]

                        }]
                    };

                    var canvasDoughnut = new Chart(ctx, {
                        type: 'doughnut',
                        tooltipFillColor: "rgba(51, 51, 51, 0.55)",
                        data: data
                    });

                },
                error: function(xhr, error){
                    bootbox.alert(xhr.responseText);
                }
            });



        }
    };

    var updateProductionChart = function(){
        if ($('#lineChart').length ){

            $.ajax( {
                url: 'home/premiumProduction',
                type: 'GET',
                processData: false,
                contentType: false,
                success: function (s ) {
                    var months = [];
                    var production = [];
                    for(var i=0;i < s.length;i++){
                        months.push(s[i].month+"/"+s[i].year);
                        production.push(s[i].premium);
                    }
                    var ctx = document.getElementById("lineChart");
                    var lineChart = new Chart(ctx, {
                        type: 'bar',
                        data: {
                            labels: months,
                            datasets: [{
                                label: "Premium Production for the Year",
                                backgroundColor: "rgba(0,51,102)",
                                data: production
                            }]
                        },
                        options: {
                            scales: {
                                yAxes: [{
                                    ticks: {
                                        callback: function(value, index, values) {
                                            if(parseInt(value) > 999){
                                                return  value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                                            } else if (parseInt(value) < -999) {
                                                return  Math.abs(value).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                                            } else {
                                                return  value;
                                            }
                                        }
                                    }
                                }]
                            }
                        }
                    });

                },
                error: function(xhr, error){
                    bootbox.alert(xhr.responseText);
                }
            });



        }
    };

    var getDashboardDetails = function(){
        $.ajax( {
            url: 'home/dashboardDetails',
            type: 'GET',
            async: true,
            processData: false,
            contentType: false,
            success: function (s ) {
                $("div.pending-quote").html(UTILITIES.currencyFormat(s.sumAssuredYTD));
                $("div.expired-pols").html(s.policiesSold);
                $("div.pend-endorse").html(UTILITIES.currencyFormat(s.premiumYTD));

            },
            error: function(xhr, error){
                if(xhr)
                    bootbox.alert(xhr.responseText);
            }
        });
    };

    var init_calendar = function(){
        if( typeof ($.fn.fullCalendar) === 'undefined'){ return; }

        var date = new Date(),
            d = date.getDate(),
            m = date.getMonth(),
            y = date.getFullYear(),
            started,
            categoryClass;

        var calendar = $('#calendar').fullCalendar({
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'month,agendaWeek,agendaDay,listMonth'
            },
            selectable: true,
            selectHelper: true,
            select: function(start, end, allDay) {
                $('#fc_create').click();

                started = start;
                var ended = end;

                $(".antosubmit").on("click", function() {
                    var title = $("#title").val();
                    if (end) {
                        ended = end;
                    }

                    categoryClass = $("#event_type").val();

                    if (title) {
                        calendar.fullCalendar('renderEvent', {
                                title: title,
                                start: started,
                                end: end,
                                allDay: allDay
                            },
                            true // make the event "stick"
                        );
                    }

                    $('#title').val('');

                    calendar.fullCalendar('unselect');

                    $('.antoclose').click();

                    return false;
                });
            },
            eventClick: function(calEvent, jsEvent, view) {
                $('#fc_edit').click();
                $('#title2').val(calEvent.title);

                categoryClass = $("#event_type").val();

                $(".antosubmit2").on("click", function() {
                    calEvent.title = $("#title2").val();

                    calendar.fullCalendar('updateEvent', calEvent);
                    $('.antoclose2').click();
                });

                calendar.fullCalendar('unselect');
            },
            editable: true,
            events: [{
                title: 'All Day Event',
                start: new Date(y, m, 1)
            }, {
                title: 'Long Event',
                start: new Date(y, m, d - 5),
                end: new Date(y, m, d - 2)
            }, {
                title: 'Meeting',
                start: new Date(y, m, d, 10, 30),
                allDay: false
            }, {
                title: 'Lunch',
                start: new Date(y, m, d + 14, 12, 0),
                end: new Date(y, m, d, 14, 0),
                allDay: false
            }, {
                title: 'Birthday Party',
                start: new Date(y, m, d + 1, 19, 0),
                end: new Date(y, m, d + 1, 22, 30),
                allDay: false
            }, {
                title: 'Click for Google',
                start: new Date(y, m, 28),
                end: new Date(y, m, 29),
                url: 'http://google.com/'
            }]
        });
    };

    var init = function(){
        getDashboardDetails();
        updateProductionChart();
        updateProductsDoughnut();
        updateBranchesDoughnut();
        // init_calendar();
        getUserTickets();
    };

    return {
        init: init
    }

})(jQuery);

jQuery(HomeScreen.init);