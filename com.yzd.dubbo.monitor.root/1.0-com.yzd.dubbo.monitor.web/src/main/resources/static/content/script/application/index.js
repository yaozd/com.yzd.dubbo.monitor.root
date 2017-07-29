/**
 * Created by zd.yao on 2017/7/18.
 */
$(function () {
    initData();
    initeCharts();
    applicationClick();
    inputFunction();
});
//region-初始化数据
function initData(){
    $.get("/application/getAllAPPAndRelation", function(resultVO){
        allAppResutMap=resultVO.data;
        dataToView(resultVO.data);
    });
}
function dataToView(allAppResutMap){
    $('#appSumNumber').html(allAppResutMap.appSum);
    $('#groupSumNumber').html(allAppResutMap.groupSum);
    // 拼接所有app数据
    var noHtml = '<span class="badge badge-danger">无</span>';
    var numberHtml = '<span class="badge badge-success">NUMBER</span>';
    var providers_category_html = '<span class="badge badge-danger providers">提供者</span>';
    var consumers_category_html = '<span class="badge badge-success consumers">消费者</span>';
    //
    var appList = allAppResutMap.appList;
    var map = {
        list: appList,
        categoryFunc: function () {
            var categoty_html = '';
            var isProvider = this.isProvider;
            var isConsumer = this.isConsumer;
            if (isProvider) categoty_html += providers_category_html;
            if (isConsumer) categoty_html += consumers_category_html;
            return categoty_html;
        },
        serviceSumFunc: function () {
            var sum = Number(this.serviceSum);
            if (sum == 0) return noHtml;
            return numberHtml.replace('NUMBER', sum);
        },
        providerSumFunc: function () {
            var sum = Number(this.providerSum);
            if (sum == 0) return noHtml;
            return numberHtml.replace('NUMBER', sum);
        },
        consumerSumFunc: function () {
            var sum = Number(this.consumerSum);
            if (sum == 0) return noHtml;
            return numberHtml.replace('NUMBER', sum);
        }
    };
    var html = Mustache.render($('#main_app_list_template').html(), map);
    $("#main_application_tbody").html(html);
}
//endregion-初始化数据
//region-初始化依赖关系图表
function initeCharts() {
    var urlPath="/content/echarts/dist";
    require.config({
        paths: {
            echarts: urlPath
        }
    });
    require(
        [
            'echarts',
            'echarts/chart/line',   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
            'echarts/chart/bar',
            'echarts/chart/chord',
            'echarts/chart/force'
        ],
        function (ec) {
            allAPPRelationForceChart(ec);

        }
    )
}
//所有app之间的依赖关系
function allAPPRelationForceChart(ec) {
    $.ajax({
        url: "/application/getAllAPPAndRelation",
        success: function (resultVO) {
            if (!resultVO.success) {
                $("#all_app_relation_force_echarts").html("加载失败~！原因：" + resultVO.msg);

            } else {
                if (ec == undefined) {
                    ec = echartsEc;
                }
                var charts_id = 'all_app_relation_force_echarts';
                var myChart = ec.init(document.getElementById(charts_id));
                myChart.showLoading({
                    text: 'Loading...',
                    effect: 'bubble',
                    textStyle: {
                        fontSize: 20
                    }
                });
                allAppResutMap = resultVO.data;
                var appMap = allAppResutMap.allApp;
                var nodesList = [];
                var linkList = [];
                $.each(appMap, function (key, value) {
                    var consumersSet = value.consumersSet;
                    var nodes_value = 1;
                    if (consumersSet != undefined) {
                        nodes_value = consumersSet.length * 1.2;
                    }
                    var categoryVal=0;
                    if(value.isProvider==false&&value.isConsumer)
                    {
                        categoryVal=1;
                    }
                    if(value.isProvider&&value.isConsumer)
                    {
                        categoryVal=2;
                    }
                    var nodesMap = {
                        category: categoryVal, name: key, value: nodes_value, draggable: true
                    };
                    nodesList.push(nodesMap);

                    if (consumersSet != undefined) {
                        $.each(consumersSet, function (i, target_value) {
                            var linkMap = {
                                source: key,
                                target: target_value,
                                weight: 1,
                                name: key + "提供服务" + target_value,
                                itemStyle: {normal: {color: 'red'}}
                            };
                            linkList.push(linkMap);
                        });
                    }
                });
                var option = {
                    title : {
                        text: 'DUBBO应用依赖关系图',
                        x:'right',
                        y:'bottom'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: ' {b}'
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            restore: {show: true},
                            magicType: {show: true, type: ['force', 'chord']},
                            saveAsImage: {show: true}
                        }
                    },
                    legend: {
                        x: 'left',
                        data:['提供者节点','消费者节点','提供者与消费者节点']
                    },
                    series: [
                        {
                            type: 'force',
                            name: "依赖关系",
                            ribbonType: false,
                            categories: [
                                {
                                    name: '提供者节点'
                                },
                                {
                                    name: '消费者节点'
                                },
                                {
                                    name:'提供者与消费者节点'
                                }
                            ],
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true,
                                        textStyle: {
                                            color: '#333'
                                        }
                                    },
                                    nodeStyle: {
                                        brushType: 'both',
                                        borderColor: 'yellow',
                                        borderWidth: 1
                                    }
                                },
                                emphasis: {
                                    label: {
                                        show: false
                                    }
                                }
                            },
                            useWorker: false,
                            minRadius : 15,
                            maxRadius : 30,
                            gravity: 1.1,
                            scaling: 1.5,
                            roam: 'move',
                            gravity: 1.1,
                            draggable: true,
                            large: true,
                            linkSymbol: 'arrow',
                            steps: 10,
                            coolDown: 0.9,
                            //preventOverlap: true,
                            nodes: nodesList,
                            links: linkList
                        }
                    ]
                };
                myChart.setOption(option);
                myChart.hideLoading();
            }
        }
    });
}
//endregion-初始化依赖关系图表
//region-点击应用
function applicationClick(){
    $("#main_application_tbody").on("click","tr",function(){
        var provider_value = $(this).find(".providers").html();
        var scroll_offset;  //得到pos这个div层的offset，包含两个值，top和left
        var appName = $(this).data("appname");
        var html = Mustache.render($('#echarts_section_template').html(), {});
        $("#echarts_section").html(html);
        $('#echarts_section').removeClass("hidden");
        //
        //finalSectionFunction();
        //
        scroll_offset = $("#scroll_offset_anchor").offset();  //得到pos这个div层的offset，包含两个值，top和left
        if (provider_value == undefined) {
            $("#services_section").addClass("hidden");
            $("#alert_section").html(Mustache.render($('#alert_danger_template').html(), {appName: appName}));
            $("#services_app_span").text(appName);
            //aPPRelationForceChart(appName);
            //服务数据图表隐藏
            $("#tab_app_data_btn").addClass("hidden");
            $("#tab_app_ranking_btn").addClass("hidden");
        } else {
            $("#services_section").removeClass("hidden");
            $("#alert_section").html("");
            //创建应用的服务列表
            initServiceTable(appName);
            //显示所有服务
            displayAllService();
            //aPPRelationForceChart(appName);
            //服务数据图表出现
            $("#tab_app_data_btn").removeClass("hidden");
            $("#tab_app_ranking_btn").removeClass("hidden");
        }
        $('body').animate({
            scrollTop: scroll_offset.top-100  //让body的scrollTop等于pos的top，就实现了滚动
        }, 1200);
    });
}
//===========================二级方法=================
// 获得该app的service列表
function initServiceTable(appName) {
    var appMap = allAppResutMap.allApp;
    var appBO = appMap[appName];
    var serviceMap = appBO.serviceMap;
    var hostList=appBO.hostList;
    var tabs_list = [];
    var tab_first = true;

    var content_list = [];
    var tab_content_first = true;
    var status_name = {'online':'线上','test':'测试','local':'本地','wrong':'错误异常'}
    $.each(serviceMap,function(status,serviceSet){
        //标签头
        var tab_map = {'name':status_name[status],'status':status,'class':''};
        if(tab_first){
            tab_map['class'] = 'active';
            tab_first = false;
        }
        tabs_list.push(tab_map);
        //内容
        var content_map = {'name':status_name[status],'status':status,'class':''};
        if(tab_content_first){
            content_map['class'] = 'active';
            tab_content_first = false;
        }
        if(status == 'wrong'){
            content_map['wrong'] = 'wrong';
        }

        //拼接每一行的内容
        var indexs = 0;
        var map = {
            list: serviceSet,
            indexFunc: function () {
                return indexs += 1
            },
            consumersFunc: function () {
                var isConsumers = this.isConsumer;
                if (isConsumers) return '<span class="badge badge-danger"><i class="fa fa-check"></i> </span>'
            },
            wrongFunc:function(){
                var wrongReason = this.wrongReason;
                if (wrongReason != undefined && wrongReason!= ''){
                    return wrongReason;
                }
            },
            methodFunc:function(){
                var html = "";
                var methodsHost = this.methodsHost;
                $.each(methodsHost,function(method,hostSet){
                    html += method +"-----";
                    $.each(hostSet,function(i,host){
                        html += host.hostString+" ";
                    })
                })
                return html;
            },
            hostFunc:function(){
                var html = "";
                var methodsHost = this.methodsHost;
                $.each(methodsHost,function(method,hostSet){
                    html += "-----";
                    $.each(hostSet,function(i,host){
                        html += host.hostString+" ";
                    })
                })
                return html;
            }

        };
        var tbody_html = Mustache.render($('#service_table_tbody_template').html(), map);
        content_map['tbody_html'] = tbody_html;
        var service_address_html = Mustache.render($('#service_address_templates').html(), {'list':hostList});
        content_map['service_address_html'] = service_address_html;
        content_list.push(content_map);
    });

    var tab_html = Mustache.render($('#service_tab_templates').html(), {'list':tabs_list});
    var content_html = Mustache.render($('#service_content_templates').html(), {'list':content_list});

    $('#all_service_div').html(tab_html+content_html);

    $("#services_app_span").text(appName);

    $(".service").unbind("click").click(function () {
        $(this).next("tr").toggleClass("hidden");
        return false;
    });
    $(".service_tab").unbind("click").click(function () {
        var content_id = $(this).find('a').attr("href");

        $('.service_tab').removeClass("active");
        $(this).addClass("active");
        $('.service_content').removeClass("active");
        $(content_id).addClass("active");

        //重置筛选
        $("#search_service_value").val('');
        $(".service").removeClass("hidden");
        return false;
    });
    $('.Tooltip').tooltip()
}
// 筛选主表格的app名称
function filterAppTable() {
    var key_value = $("#search_app_value").val().trim().toUpperCase();
    var all_tr = $('#main_application_tbody > tr');
    if (key_value == '') {
        $(all_tr).removeClass("hidden");
    } else {
        $.each(all_tr, function (i, obj) {
            var value = $(obj).data("appname").toUpperCase();
            if (value.indexOf(key_value) == -1) {
                $(obj).addClass("hidden");
            } else {
                $(obj).removeClass("hidden");
            }
        });
    }
    return false;
}
//endregion-点击应用

function inputFunction() {
    $('#search_app_value').keyup(function () {
        filterAppTable();
        return false;
    });
    $('#search_service_value').keyup(function () {
        searchService()
        return false;
    });
    $(document).on("click",'#service_address_id span',function(){
        var value = $(this).data("serviceaddress");
        $('#search_service_value').val(value);
        searchService();
    });
};
function displayAllService(){
    $('#search_service_value').val("");
    searchService();
}
function searchService(){
    var key_value = $("#search_service_value").val().trim().toUpperCase();
    var all_tr = $('#all_service_div > div > div.active>div>table>tbody>tr.service');
    if (key_value == '') {
        $(all_tr).removeClass("hidden");
    } else {
        $.each(all_tr, function (i, obj) {
            var value = $(obj).data("servicename").toUpperCase();
            //console.log(value);
            if (value.indexOf(key_value) == -1) {
                $(obj).addClass("hidden");
            } else {
                $(obj).removeClass("hidden");
            }
        });
    }
}