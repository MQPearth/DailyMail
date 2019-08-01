package com.github.mqpearth.dao;

import com.github.mqpearth.util.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Calendar;

import java.util.List;


/**
 * 从网页中提取数据
 */
@Repository
public class ApiDao {

    @Autowired
    private DateUtils dateUtils;

    private String cityIdInterface = "http://toy1.weather.com.cn/search?cityname=";  //城市id接口
    private String weatherInterface = "http://www.weather.com.cn/weather1d/{id}.shtml";//天气信息接口
    private String sentenceInterface = "https://tool.lu/timestamp/";
    private String blogInterface = "https://www.oschina.net/blog?tab=daily";

    /**
     * 根据城市名查询城市id
     *
     * @param city 城市名
     * @return 具体的天气数据
     */
    public String getCityId(String city) throws IOException {

        //忽略响应类型发起get请求
        Document document = Jsoup.connect(cityIdInterface + city).ignoreContentType(true)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                .get();
        //获取响应数据
        String response = document.toString();
        //对响应数据进行切分
        String[] quotesSplit = response.split("\"");
        if (quotesSplit.length > 4) {  //接口数据正常获取
            String[] split = quotesSplit[3].split("~");
            if (split.length == 10) {//接口数据正常获取
                return split[0];
            } else
                throw new IOException("城市id接口异常");
        } else
            throw new IOException("城市id接口异常");
    }

    /**
     * 根据城市id查询城市天气具体信息
     *
     * @param cityId
     * @return
     */
    public String getWeatherByCityId(String cityId) throws IOException {
        Document document = Jsoup.connect(weatherInterface.replaceAll("\\{id}", cityId))
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                .get();
        Elements weatherElements = document.getElementsByClass("wea");
        //获取当天天气
        String weather = null;
        for (String s : weatherElements.eachText())
            weather = "<h4>天气 [" + s + "]</h4>";

        if (null == weather)
            throw new IOException("天气信息天气接口异常");

        //获取当天温度
        Elements temperatureElements = document.getElementsByClass("tem");
        String temperature = null;
        List<String> temperatureList = temperatureElements.eachText();
        if (temperatureList.size() <= 0)
            throw new IOException("天气信息温度接口异常");
        temperature = "<h4>温度 [" + temperatureList.get(0) + " ~ " + temperatureList.get(1) + "]</h4>";


        //获取风力
        Elements windElements = document.getElementsByClass("win");
        String wind = null;
        List<String> windLevelList = windElements.eachText(); //风力等级

        if (windLevelList.size() <= 0)
            throw new IOException("天气信息风力接口异常");

        if (windElements.size() <= 0)
            throw new IOException("天气信息风向接口异常");

        Element dayElement = windElements.get(0);  //白天风向
        Element nightElement = windElements.get(1);  //夜晚风向

        wind = "<h4>白天风力 [" + dayElement.getElementsByAttribute("title").attr("title") + windLevelList.get(0) + "]</h4>" +
                "<h4>夜间风力 [" + nightElement.getElementsByAttribute("title").attr("title") + windLevelList.get(1) + "]</h4>";


        String ultraviolet = null; // 紫外线指数
        Elements ultravioletElements = document.getElementsByClass("li1 hot");
        for (String s : ultravioletElements.eachText())
            ultraviolet = "<h4>紫外线指数 " + ("[" + s.replaceAll("紫外线指数", "") + "]</h4>").replaceAll(" ", "-").replaceAll("。", "");

        if (null == ultraviolet)
            throw new IOException("天气信息紫外线指数接口异常");


        String clothes = null; //穿衣指数
        Element clothesElement = document.getElementById("chuanyi");

        if (null == clothesElement)
            throw new IOException("天气信息穿衣指数接口异常");

        clothes = "<h4>穿衣指数 " + ("[" + clothesElement.text().replaceAll("穿衣指数", "") + "]</h4>").replaceAll(" ", "-").replaceAll("。", "");


        //空气污染
        String air = null;
        Elements airElements = document.getElementsByClass("li6 hot");
        for (String s : airElements.eachText())
            air = "<h4>空气污染扩散指数 " + ("[" + s.replaceAll("空气污染扩散指数", "") + "]</h4>").replaceAll(" ", "-").replaceAll("。", "");

        if (null == air)
            throw new IOException("天气信息空气污染扩散指数接口异常");


        StringBuffer weatherInfo = new StringBuffer();
        weatherInfo.append(weather)
                .append(temperature)
                .append(wind)
                .append(ultraviolet)
                .append(clothes)
                .append(air);


        return weatherInfo.toString();
    }

    /**
     * 获取今天日期
     * 格式2019年7月16日，农历
     *
     * @return
     */
    public String getDate() throws IOException {
        StringBuffer dateInfo = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        dateInfo.append(calendar.get(Calendar.YEAR) + "年");//获取年
        dateInfo.append(calendar.get(Calendar.MONTH) + 1 + "月");//获取月
        dateInfo.append(calendar.get(Calendar.DATE) + "日");//获取日

        String nongli = dateUtils.getNongLi(dateInfo.toString());

        if (null == nongli)
            throw new IOException("农历计算错误");

        dateInfo.append("，农历").append(nongli).append("\n");

        return dateInfo.toString();
    }

    /**
     * 获取鸡汤
     *
     * @return
     */
    public String getSentence() throws IOException {

        Document document = Jsoup.connect(sentenceInterface)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                .get();

        Elements elements = document.getElementsByClass("note-container");
        List<String> list = elements.eachText();

        for (String sentence : list)
            return sentence.replaceAll("【", "").replaceAll("】", " ");

        throw new IOException("鸡汤接口异常");
    }

    /**
     * 获取开源中国 推荐博客列表的最近十条记录
     *
     * @return
     */
    public String getBlog() throws IOException {
        Document document = Jsoup.connect(blogInterface)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                .get();
        Elements blogElements = document.getElementsByClass("ui basic segment  tab article-list");
        Element element = blogElements.get(0);
        Elements header = element.getElementsByClass("header");

        StringBuffer blogList = new StringBuffer();
        for (int i = 0; i < 10; i++) {

            Element element1 = header.get(i);

            String title = element1.text();
            int yuanIndex = title.lastIndexOf("原");
            int jianIndex = title.lastIndexOf("荐");
            char[] titleChars = title.toCharArray();
            titleChars[yuanIndex] = ' ';
            titleChars[jianIndex] = ' ';
            String replaceTitle = new String(titleChars);

            blogList.append("<a style=\"text-decoration:none \" href=\"").append(element1.attr("href")).append("\">")
                    .append(i + 1).append(". ").append(replaceTitle.replaceAll(" ", ""))
                    .append("</a><br />");

        }

        return blogList.toString();
    }
}
