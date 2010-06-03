/**
 * Copyright (c) 2008-2010, http://code.google.com/p/snakeyaml/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package examples.jodatime;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

class JodaTimeRepresenter extends Representer {
    public JodaTimeRepresenter() {
        multiRepresenters.put(DateTime.class, new RepresentJodaDateTime());
    }

    private class RepresentJodaDateTime implements Represent {

        public Node representData(Object data) {
            DateTime dt = (DateTime) data;
            int years = dt.getYear();
            int months = dt.getMonthOfYear();
            int days = dt.getDayOfMonth();
            int hour24 = dt.getHourOfDay();
            System.out.println(hour24);
            int minutes = dt.getMinuteOfHour();
            int seconds = dt.getSecondOfMinute();
            int millis = dt.getMillisOfSecond();
            StringBuilder buffer = new StringBuilder(String.valueOf(years));
            buffer.append("-");
            if (months < 10) {
                buffer.append("0");
            }
            buffer.append(String.valueOf(months));
            buffer.append("-");
            if (days < 10) {
                buffer.append("0");
            }
            buffer.append(String.valueOf(days));
            buffer.append("T");
            if (hour24 < 10) {
                buffer.append("0");
            }
            buffer.append(String.valueOf(hour24));
            buffer.append(":");
            if (minutes < 10) {
                buffer.append("0");
            }
            buffer.append(String.valueOf(minutes));
            buffer.append(":");
            if (seconds < 10) {
                buffer.append("0");
            }
            buffer.append(String.valueOf(seconds));
            if (millis > 0) {
                if (millis < 10) {
                    buffer.append(".00");
                } else if (millis < 100) {
                    buffer.append(".0");
                } else {
                    buffer.append(".");
                }
                buffer.append(String.valueOf(millis));
            }
            if (DateTimeZone.UTC.equals(dt.getZone())) {
                buffer.append("Z");
            } else {
                // Get the Offset from GMT taking DST into account
                int gmtOffset = dt.getZone().getOffset(dt);
                int minutesOffset = gmtOffset / (60 * 1000);
                int hoursOffset = minutesOffset / 60;
                int partOfHour = minutesOffset % 60;
                if (hoursOffset > 0) {
                    buffer.append("");
                }
                buffer.append(hoursOffset + ":");
                if (partOfHour < 10) {
                    buffer.append("0" + partOfHour);
                } else {
                    buffer.append(partOfHour);
                }
            }
            return representScalar(getTag(data.getClass(), Tag.TIMESTAMP), buffer.toString(), null);
        }
    }

}