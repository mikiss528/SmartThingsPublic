/**
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Space Heater Control
 *
 *  Author: MIKISS528
 */
definition(
    name: "Space Heater Control",
    namespace: "MIKISS528",
    author: "Michael K. Son",
    description: "Control a space heater in conjunction with any temperature sensor, like a SmartSense Multi.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/temp_thermo-switch.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/temp_thermo-switch@2x.png"
)

preferences {
	section("Choose a temperature sensor... "){
		input "TempSensor", "capability.temperatureMeasurement", title: "TempSensor"
	}
	section("Select the heater or air conditioner outlet(s)... "){
		input "HeaterOutlet", "capability.switch", title: "HeaterOutlet", multiple: true
	}
	section("Set the Highest temperature..."){
		input "TempHigh", "decimal", title: "TempHigh"
	}
	section("Set the Lowest temperature..."){
		input "TempLow", "decimal", title: "TempLow"
	}
	section("Within this number of minutes..."){
		input "Interval", "number", title: "Interval", required: false
	}
	section("Turn on between what times:"){
                input "TimeStart", "time", title: "TimeStart", required: true
                input "TimeEnd", "time", title: "TimeEnd", required: true
        }
}

def installed() {
     log.debug "Installed with settings: ${settings}"
     initialize()
}

def updated() {
     log.debug "Updated with settings: ${settings}"
     unsubscribe()
     initialize()
}

def initialize() {
     subscribe("TempSensor", "temperature", temperatureHandler)
}

def temperatureHandler(evt) {
     def between = timeofDayIsBetween(TimeStart, TimeEnd, new Date(), location.timeZone)
     def CurrentTemp = TempSensor.currentTemperature
     if (between){
          if(CurrentTemp < TempLow){
               log.debug "Current Temp : ${CurrentTemp}"
               HeaterOutlet.on()}
          else if (CurrentTemp > TempHigh){
               log.debug "Current Temp : ${CurrentTemp}"
               HeaterOutlet.off()}
          else {
               log.debug "Current Temp : ${CurrentTemp}"}
     }
     else {
          log.debug "Current Temp : ${CurrentTemp}"
          HeaterOutlet.off()
     }
}