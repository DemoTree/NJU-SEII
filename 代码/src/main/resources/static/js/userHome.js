window.onload=function()
{
    getmovieboxoffice();
    getmovielist();
    getthank()
    function getmovieboxoffice() {
        getRequest(
            '/statistics/boxOffice/total',
            function (res) {
                var data = res.content || [];
               $(".top-sell-list").empty();
               var str="";
               data.forEach(function (one) {
                   if (one.boxOffice==null){
                       var qstr=0
                   } else {
                       var qstr=one.boxOffice
                   }
                   str+=
                       "<div class=\"statistic-item\">"+
                       "<span>"+one.name+"</span>"+
                       "<span class=\"error-text\">"+qstr+"</span>"+
                       "</div>"



               })

                $(".top-sell-list").append(str);




            },
            function (error) {
                alert(JSON.stringify(error));
            });

    }





    function getmovielist() {
        getRequest(
            '/movie/all',
            function (res) {
               var data=res.content||[];
               for (var i=0;i<6&&i<data.length;i++){
                   var url=data[i].posterUrl;
                   var a=document.getElementById("img"+(i+1));
                   a.src=url;
                   document.getElementById("aofimg"+(i+1)).href="/user/movieDetail?id="+data[i].id;

               }
            },
            function (error) {
                alert(error);
            }
        );

    }





    function getthank() {
        getRequest(
            '/getvipconsumeforuserhome',
            function (res) {

                console.log(res.content)
                $(".top-expectation-list").empty();
                var str=
                    "<div class=\"statistic-item\">"+
                    "<span>用户名称</span>"+
                    "<span class=\"error-text\">消费金额</span>"+
                    "</div>";
                res.content.forEach(function (one) {
                    str+=
                        "<div class=\"statistic-item\">"+
                        "<span>"+one.name+"</span>"+
                        "<span class=\"error-text\">"+one.consume+"</span>"+
                        "</div>"


                })

                $(".top-expectation-list").append(str);
            },
            function (error) {
                alert(error);
            }
        );


    }
    var oPlay=document.getElementById('play');
    var aLi=oPlay.getElementsByClassName('ali');
    var oButton=document.getElementById('button');
    var aDiv=oButton.getElementsByClassName('adiv');
    var oPrev=document.getElementById('prev');
    var oNext=document.getElementById('next');
    var oFlash=document.getElementById('flash');
    var now=0;
    var timer2=null;
    for(var i=0; i<aDiv.length; i++) {
        aDiv[i].index=i;
        aDiv[i].onmouseover=function(){
            if(now==this.index) return;
            now=this.index;
            tab();
        }
    }
    oPrev.onclick=function(){
        now--;
        if(now==-1){
            now=aDiv.length-1;
        }
        tab();
    }
    oNext.onclick=function(){
        now++;
        if(now==aDiv.length){
            now=0;
        }
        tab();
    }
    oFlash.onmouseover=function()
    {
        clearInterval(timer2);
    }
    oFlash.onmouseout=function()
    {
        timer2=setInterval(oNext.onclick,4000);
    }
    timer2=setInterval(oNext.onclick,5000);
    function tab(){
        for(var i=0; i<aLi.length; i++){
            aLi[i].style.display='none';
        }
        for(var i=0; i<aDiv.length; i++) {
            aDiv[i].style.background="#DDDDDD";
        }
        aDiv[now].style.background='#A10000';
        aLi[now].style.display='block';
        aLi[now].style.opacity=0;
        aLi[now].style.filter="alpha(opacity=0)";
        jianbian(aLi[now]);
    }
    function jianbian(obj){
        var alpha=0;
        clearInterval(timer);
        var timer=setInterval(function(){
            alpha++;
            obj.style.opacity=alpha/100;
            obj.style.filter="alpha(opacity="+alpha+")";
            if(alpha==100) {
                clearInterval(timer);
            }
        },10);
    }
}