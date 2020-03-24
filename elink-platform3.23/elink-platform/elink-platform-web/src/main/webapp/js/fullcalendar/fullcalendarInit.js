function initFullcalendar(calendar){
	$('#'+calendar).fullCalendar({
	      defaultDate: new Date().format("yyyy-MM-dd"),
	      editable:true,
	      eventLimit:true,
	      navLinks: true,
	      /*  
		      设置日历头部信息  
		      头部信息包括left、center、right三个位置，分别对应头部左边、头部中间和头部右边。  
		      头部信息每个位置可以对应以下配置：  
		          title: 显示当前月份/周/日信息  
		          prev: 用于切换到上一月/周/日视图的按钮  
		          next: 用于切换到下一月/周/日视图的按钮  
		          prevYear: 用于切换到上一年视图的按钮  
		          nextYear: 用于切换到下一年视图的按钮  
		      如果不想显示头部信息，可以设置header为false  
	     */ 
	      header: {
	          left: 'prev,next today',
	          center: 'title',
	          right: 'listDay,listWeek,month'
	      },
	      views: {
	          listDay: { buttonText: '日' },
	          listWeek: { buttonText: '周' }
	      },
	      monthNames:["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
	      monthNamesShort:["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
	      dayNames:["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
	      dayNamesShort:["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
	      //显示周末，设为false则不显示周六和周日。  
	      weekends: true,  
	      //日历上显示全天的文本  
	      allDayText: '全天',
	      firstDay: 1,
	      buttonText: {
	        today: '今天',
	        month: '月'
	      },
	      timeFormat: 'H(:mm)',
	      events: function(start, end, timezone, callback) {
	    	  loadScheduleData(start.format(),end.format(),callback);
	      }
	    });
}