import java.util.Calendar;
class CalendarBean { //三个属性，三个方法
    int year = 2010;
    int month = 0;
    int nextday;
    public void setYear(int year){
        this.year = year;
    }
    public void setMonth(int month){
        this.month = month;
    }
    public String[][] getCalendar(){//返回二维表形式的日历给table
        String[][] a = new String[6][7];
        for(int i = 0; i < 6;i++)
            for(int j = 0;j < 7;j++)
                a[i][j] = "";//为后面做判断初始化
        Calendar cal = Calendar.getInstance();
        cal.set(year,month-1,1);//month要减1 1-12 -> 0-11
        int weekwhat = cal.get(Calendar.DAY_OF_WEEK) - 1; //1-7 -> 0-6 //1-7是按美式日历星期天到星期六
        int day = 0;
        if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
            day = 31;
        else if(month == 4 || month == 6 || month == 9 || month == 11)
            day = 30;
        else{
            if((year % 4 == 0 && year % 100 != 0)||(year % 400 == 0))
                day = 29;
            else
                day = 28;
        } //day 判断

        //start display rili
        nextday = 1;
        for(int k = 0;k < 6;k++){
            if(k == 0)
                for(int j = weekwhat;j < 7;j++){ //单独判断第一行，其他行按条件添加即可
                    a[k][j] = ""+nextday;
                    nextday++;
                }
            else
                for(int j = 0;j < 7 && nextday <= day;j++){
                    a[k][j] = ""+nextday;
                    nextday++;
                }
        }
        return a; //返回二维数组名
    }
}
