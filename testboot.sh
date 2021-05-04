for i in `seq 1 100`
do 
	adb shell am force-stop mobi.cangol.mobile.uiframe.demo
	sleep 1
	adb shell am start-activity -W -n mobi.cangol.mobile.uiframe.demo/.AllActivity | grep "TotalTime" | cut -d ' ' -f 2
done