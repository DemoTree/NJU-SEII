package com.example.cinema.blImpl.statistics;

import com.example.cinema.bl.statistics.StatisticsService;
import com.example.cinema.data.management.HallMapper;
import com.example.cinema.data.management.MovieMapper;
import com.example.cinema.data.management.ScheduleMapper;
import com.example.cinema.data.sales.TicketMapper;
import com.example.cinema.data.statistics.StatisticsMapper;
import com.example.cinema.po.*;
import com.example.cinema.vo.*;
import com.example.cinema.po.Movie;
import com.example.cinema.po.MovieBoxOffice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author fjj
 * @date 2019/4/16 1:34 PM
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private HallMapper hallMapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private MovieMapper movieMapper;
    @Override
    public ResponseVO getScheduleRateByDate(Date date) {
        try{
            Date requireDate = date;
            if(requireDate == null){
                requireDate = new Date();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            requireDate = simpleDateFormat.parse(simpleDateFormat.format(requireDate));

            Date nextDate = getNumDayAfterDate(requireDate, 1);
            return ResponseVO.buildSuccess(movieScheduleTimeList2MovieScheduleTimeVOList(statisticsMapper.selectMovieScheduleTimes(requireDate, nextDate)));

        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }

    @Override
    public ResponseVO getTotalBoxOffice() {
        try {
            return ResponseVO.buildSuccess(movieTotalBoxOfficeList2MovieTotalBoxOfficeVOList(statisticsMapper.selectMovieTotalBoxOffice()));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
            //返回了一个含有所有电影票房纪录的list（封装在responsevo中）
        }
    }

    @Override
    public ResponseVO getAudiencePriceSevenDays() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            Date startDate = getNumDayAfterDate(today, -6);
            List<AudiencePriceVO> audiencePriceVOList = new ArrayList<>();
            for(int i = 0; i < 7; i++){
                AudiencePriceVO audiencePriceVO = new AudiencePriceVO();
                Date date = getNumDayAfterDate(startDate, i);
                audiencePriceVO.setDate(date);
                List<AudiencePrice> audiencePriceList = statisticsMapper.selectAudiencePrice(date, getNumDayAfterDate(date, 1));
                double totalPrice = audiencePriceList.stream().mapToDouble(item -> item.getTotalPrice()).sum();
                audiencePriceVO.setPrice(Double.parseDouble(String.format("%.2f", audiencePriceList.size() == 0 ? 0 : totalPrice / audiencePriceList.size())));
                audiencePriceVOList.add(audiencePriceVO);
            }
            return ResponseVO.buildSuccess(audiencePriceVOList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }




    /**
     * TODO:获取所有电影某天的上座率
     * 上座率参考公式：假设某影城设有n 个电影厅、m 个座位数，相对上座率=观众人次÷放映场次÷m÷n×100%
     * 此方法中运用到的相应的操作数据库的接口和实现请自行定义和实现，相应的结果需要自己定义一个VO类返回给前端
     * @param date
     * @return
     */
    @Override
    public ResponseVO getMoviePlacingRateByDate(Date date) {
        try {


            /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = simpleDateFormat.parse(date.toString());//这部分不知到要不要*/
            List<Movie> movieList=movieMapper.selectOtherMoviesExcludeOff();
            List<MoviePlacing> moviePlacingList=new ArrayList<>();
            int indexofmovie=0;
            for (indexofmovie=0;indexofmovie<movieList.size();indexofmovie++){
                moviePlacingList.add(new MoviePlacing(movieList.get(indexofmovie).getId(),movieList.get(indexofmovie).getName()));
            }


        List<Hall> allHall=hallMapper.selectAllHall();//获取所有影厅信息
        int indexofhall=0;
        int indexofschedule=0;
        int sumSeat=0;
        int indexofmovieplacinglist;
        List<ScheduleItem> scheduleItemsofthehall=new ArrayList<>();
        List<Ticket> ticketList;
        for(indexofhall=0;indexofhall<allHall.size();indexofhall++){
            sumSeat+=allHall.get(indexofhall).getColumn()*allHall.get(indexofhall).getRow();
            scheduleItemsofthehall=scheduleMapper.selectSchedule(allHall.get(indexofhall).getId(),date,getNumDayAfterDate(date,1));//按影厅获取当日所有排片信息
            for(indexofschedule=0;indexofschedule<scheduleItemsofthehall.size();indexofschedule++){
                ticketList=ticketMapper.selectTicketsBySchedule(scheduleItemsofthehall.get(indexofschedule).getId());//根据排片查询卖出的票数
                for (indexofmovieplacinglist=0;indexofmovieplacinglist<moviePlacingList.size();indexofmovieplacinglist++){
                    if (moviePlacingList.get(indexofmovieplacinglist).getMovieId()==scheduleItemsofthehall.get(indexofschedule).getMovieId()){
                        moviePlacingList.get(indexofmovieplacinglist).addnumofschedule();
                        moviePlacingList.get(indexofmovieplacinglist).addnumofaudience(ticketList.size());
                        break;
                    }
                }
            }
        }

        List<MoviePlacingVO> moviePlacingVOList=new ArrayList<>();
        MoviePlacingVO moviePlacingVO;
        for(indexofmovieplacinglist=0;indexofmovieplacinglist<moviePlacingList.size();indexofmovieplacinglist++){
            moviePlacingVO=new MoviePlacingVO();
            moviePlacingVO.setMovieName(moviePlacingList.get(indexofmovieplacinglist).getMovidName());
            moviePlacingVO.setPalcingrate((((double)moviePlacingList.get(indexofmovieplacinglist).getNumofaudience()/(double)moviePlacingList.get(indexofmovieplacinglist).getNumofschedule())/sumSeat)*100);
            moviePlacingVOList.add(moviePlacingVO);

        }





        return ResponseVO.buildSuccess(moviePlacingVOList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }
    }









    /**
     * TODO:获取最近days天内，最受欢迎的movieNum个电影(可以简单理解为最近days内票房越高的电影越受欢迎)
     * 此方法中运用到的相应的操作数据库的接口和实现请自行定义和实现，相应的结果需要自己定义一个VO类返回给前端
     * @param days
     * @param movieNum
     * @return
     */
    @Override
    public ResponseVO getPopularMovies(int days, int movieNum) {
        List<Movie> movieList=new ArrayList<>();
        try {
        movieList=movieMapper.selectAllMovie();

        List<MovieBoxOffice> movieBoxOffices=new ArrayList<>();

        int indexofmovie=0;
        for(indexofmovie=0;indexofmovie<movieList.size();indexofmovie++){
            movieBoxOffices.add(new MovieBoxOffice(movieList.get(indexofmovie).getId(),0.0,movieList.get(indexofmovie).getName()));
        }//创建一个暂存各个正在上映的电影的票房的list，可以随市更新
       /* SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date end = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        Date start = getNumDayAfterDate(end, -days);*/
        //上下两个不知道哪个有用，先都放在这
        Date date=new Date();

        Date start=new Date(date.getYear(),date.getMonth(),date.getDate()-days+1);

        Date end=new Date(date.getYear(),date.getMonth(),date.getDate()+1);
        List<Hall> allHall=hallMapper.selectAllHall();//获取所有影厅信息
        int indexofhall=0;
        int indexofschedule=0;
        int indexofMBlist=0;
        List<ScheduleItem> scheduleItemsofthehall;
        List<Ticket> ticketList;
        for(indexofhall=0;indexofhall<allHall.size();indexofhall++){
            scheduleItemsofthehall=scheduleMapper.selectSchedule(allHall.get(indexofhall).getId(),start,end);
            for(indexofschedule=0;indexofschedule<scheduleItemsofthehall.size();indexofschedule++){
                ticketList=ticketMapper.selectTicketsBySchedule(scheduleItemsofthehall.get(indexofschedule).getId());
                for(indexofMBlist=0;indexofMBlist<movieBoxOffices.size();indexofMBlist++){
                    if (movieBoxOffices.get(indexofMBlist).getMovieId()==scheduleItemsofthehall.get(indexofschedule).getMovieId()){
                        movieBoxOffices.get(indexofMBlist).appendBoxoffice(ticketList.size()*scheduleItemsofthehall.get(indexofschedule).getFare());
                        break;
                    }
                }
            }
        }
        List<PopularMovieVO> popularMoviesVOList=new ArrayList<>();
        PopularMovie popularMovie;
        int i=0;
        int j=0;
        int maxindex=0;
        double max=0.0;
        MovieBoxOffice maxMovieBoxffice=new MovieBoxOffice();
        Movie movie;
        int id;
        for (i=0;i<movieNum;i++){
            max=-0.1;
            maxindex=0;
            for (j=0;j<movieBoxOffices.size();j++){
                if (movieBoxOffices.get(j).getBoxOffice()>max){
                    maxMovieBoxffice=movieBoxOffices.get(j);
                    max=maxMovieBoxffice.getBoxOffice();
                    maxindex=j;
                }
            }
            popularMovie=new PopularMovie();
            popularMovie.setBoxoffice(maxMovieBoxffice.getBoxOffice());
            popularMovie.setMovieId(maxMovieBoxffice.getMovieId());
            popularMovie.setMovieName(maxMovieBoxffice.getMovieName());
            popularMoviesVOList.add(new PopularMovieVO(popularMovie));
            movieBoxOffices.remove(maxindex);

        }

        return ResponseVO.buildSuccess(popularMoviesVOList);






        }catch (Exception e){
            e.printStackTrace();
            return ResponseVO.buildFailure("失败");
        }


    }












    /**
     * 获得num天后的日期
     * @param oldDate
     * @param num
     * @return
     */
    Date getNumDayAfterDate(Date oldDate, int num){
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(oldDate);
        calendarTime.add(Calendar.DAY_OF_YEAR, num);
        return calendarTime.getTime();
    }



    private List<MovieScheduleTimeVO> movieScheduleTimeList2MovieScheduleTimeVOList(List<MovieScheduleTime> movieScheduleTimeList){
        List<MovieScheduleTimeVO> movieScheduleTimeVOList = new ArrayList<>();
        for(MovieScheduleTime movieScheduleTime : movieScheduleTimeList){
            movieScheduleTimeVOList.add(new MovieScheduleTimeVO(movieScheduleTime));
        }
        return movieScheduleTimeVOList;
    }


    private List<MovieTotalBoxOfficeVO> movieTotalBoxOfficeList2MovieTotalBoxOfficeVOList(List<MovieTotalBoxOffice> movieTotalBoxOfficeList){
        List<MovieTotalBoxOfficeVO> movieTotalBoxOfficeVOList = new ArrayList<>();
        for(MovieTotalBoxOffice movieTotalBoxOffice : movieTotalBoxOfficeList){
            movieTotalBoxOfficeVOList.add(new MovieTotalBoxOfficeVO(movieTotalBoxOffice));
        }
        return movieTotalBoxOfficeVOList;
    }
}
