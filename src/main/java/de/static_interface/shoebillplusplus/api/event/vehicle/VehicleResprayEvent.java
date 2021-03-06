/*
 * Copyright (c) 2013 - 2014 <http://static-interface.de> and contributors
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.static_interface.shoebillplusplus.api.event.vehicle;

public class VehicleResprayEvent extends VehicleEvent<net.gtaun.shoebill.event.vehicle.VehicleResprayEvent> {
    int color1;
    int color2;

    public VehicleResprayEvent(net.gtaun.shoebill.event.vehicle.VehicleResprayEvent base) {
        super(base);
        color1 = getBase().getColor1();
        color2 = getBase().getColor2();
    }

    public int getColor1() {
        return color1;
    }

    public int getColor2() {
        return color2;
    }
}
