package com.schooltools.sqlandroidstudio

class Objects {

    fun datalist():ArrayList<Date>{
        val date1=Date("Tue",23,"Apr")
        val date2=Date("Wed",24,"Apr")
        val date3=Date("Thu",25,"Apr")
        val date4=Date("Fri",26,"Apr")
        val date5=Date("Mon",27,"Apr")
        val date6=Date("Sat",28,"Apr")
        return arrayListOf(date1,date2,date3,date4,date5,date6)
    }
    fun categorylist():ArrayList<Category>{
        val ctg1=Category("All")
        val ctg2=Category("Important")
        val ctg3=Category("Lecture notes")
        val ctg4=Category("To-do lists")
        val ctg5=Category("Shopping list")
        return arrayListOf(ctg1,ctg2,ctg3,ctg4,ctg5,ctg1,ctg2,ctg3,ctg4,ctg5)
    }
}