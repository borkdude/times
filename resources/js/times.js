// stolen from http://stackoverflow.com/questions/6117814/get-week-of-year-in-javascript-like-in-php
function getWeekAndYear(d) {
    // Copy date so don't modify original
    d = new Date(d);
    d.setHours(0,0,0);
    // Set to nearest Thursday: current date + 4 - current day number
    // Make Sunday's day number 7
    d.setDate(d.getDate() + 4 - (d.getDay()||7));
    // Get first day of year
    var yearStart = new Date(d.getFullYear(),0,1);
    // Calculate full weeks to nearest Thursday
    var weekNo = Math.ceil(( ( (d - yearStart) / 86400000) + 1)/7)
    // Return array of year and week number
    return [d.getFullYear(), weekNo];
}

function loadEditProject(projectname) {
	var projname = $('#'+projectname+ ' td.name').text();
	var description =  $('#'+projectname+ ' td.description').text();
	var budget =  $('#'+projectname+ ' td.budget').text();
	
	$('#editprojectform .namefield').val(projname);
	$('#editprojectform .namefield').removeClass("error");
	$('#editprojectform .namefield + p').remove();
	
	$('#editprojectform #oldname').val(projname);
	$('#editprojectform .descriptionfield').val(description);
	
	$('#editprojectform .budgetfield').val(budget);
	$('#editprojectform .budgetfield').removeClass("error");
	$('#editprojectform .budgetfield + p').remove();
}

jQuery(function () {
	//console.log('foobarn');
	var weekAndYear = getWeekAndYear(Date.now());
	
	$('.weekfield').attr('placeholder',weekAndYear[1]);
	$('.yearfield').attr('placeholder',weekAndYear[0]);

	$('#saveweek').on('click', function(e){
	    // We don't want this to act as a link so cancel the link action
	    //e.preventDefault();
		console.log("foo");
	    // Find form and submit it
	    $('#newweekform').submit();
	  });
})

