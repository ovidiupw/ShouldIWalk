ShouldIWalk Application <development notes>:

General idea:
	Input: 
		User supplied data about trip locations:
			- start point on map
			- end point on map
		User supplied data about the way of transport:
			- walk, car, bus, subway, etc -> should be fixed choices for prediction reliability
		Data about real world conditions:
			- weather at start point
			- weather at end point
			- time, day, year
		Personal user data (based on user feedback):
			- traffic status
			- transport difficulty (e.g. snow on pavement, mud on roads, etc.)
			- did the trip take longer/less than expected?
			- were you on a rush
			- ! would you use <way of transport> again to get from <start point> to <end point>?
	Output:
		- YES if application recommends, based on data, to use <way of transport> to get from <start point> to <end point>
		- NO and alternative <ways of transport> if application does not recommend to get from <start point> to <end point> using the user-supplied <way of transport>


Architecture:
	- Android activity-based application
	- No authentication mechanism -> only anonymous data recorded
	- Needs access to internet for:
		- using google maps api
		- using weather and time reporting tools
	- Needs access to localstorage database for caching data about user in order to be able to make subsequent predictions
	- Should use popup notifications in the taskbar to request anonymous feedback from user about some trip


TODO:
- Add tests 
- Implement a stack  used for going back inside record trip data activity (should have a maximum size; when size reached, oldest visited screens are eliminated to maintain constant size)
- Finish gmaps location chooser
- Add a backwards button in the Add Trip Record screen
- Check for network connection when starting the app
	- advise against non-networked use
	- make sure application works as expected even with no network
		- enter latitude and longitude manually instead of choosing point on google maps
		- enter weather manually instead of being extracted directly from location + time combination