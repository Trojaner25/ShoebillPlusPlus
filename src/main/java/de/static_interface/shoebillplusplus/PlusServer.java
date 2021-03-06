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

package de.static_interface.shoebillplusplus;

import de.static_interface.shoebillplusplus.Warning.*;
import de.static_interface.shoebillplusplus.api.*;
import de.static_interface.shoebillplusplus.api.plugin.*;
import de.static_interface.shoebillplusplus.api.scheduler.*;
import de.static_interface.shoebillplusplus.scheduler.*;
import net.gtaun.shoebill.*;
import org.slf4j.*;

public class PlusServer implements Server {
    public static int TICK = 50;
    private static PlusServer instance;

    protected PlusServer() { }

    private PluginManager pluginManager;
    private WarningState warningState = WarningState.DEFAULT;
    private PlusScheduler scheduler;
    private boolean running = true;

    protected void init() {
        instance = this;
        pluginManager = new SimplePluginManager(this);
        scheduler = new SchedulerImpl();

        Thread taskThread = new Thread(new Runnable() {
            private int ticks;
            @Override
            public void run() {
                try {
                    while(running) {
                        ++ticks;
                        ShoebillPlusPlusPlugin.getInstance().getShoebill().runOnSampThread(()
                                                                                                   -> ((SchedulerImpl) scheduler)
                                .mainThreadHeartbeat(ticks));
                        Thread.sleep(TICK);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    });
    taskThread.start();
    }

    public static PlusServer get() {
        return instance;
    }

    @Override
    public Shoebill getShoebill() {
        return ShoebillPlusPlusPlugin.getInstance().getShoebill();
    }

    @Override
    public Logger getLogger() {
        return ShoebillPlusPlusPlugin.getInstance().getLogger(); //Todo: change this
    }

    @Override
    public boolean isPrimaryThread() {
        return Thread.currentThread().equals(ShoebillPlusPlusPlugin.getInstance().getPrimaryThread());
    }

    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public WarningState getWarningState() {
        return warningState;
    }

    @Override
    public String getVersion() {
        return ShoebillPlusPlusPlugin.getInstance().getDescription().getVersion();
    }

    @Override
    public void shutdown() {
        destroy();
        net.gtaun.shoebill.object.Server.get().sendRconCommand("exit"); //todo: bad implementation?
    }

    @Override
    public String getServerName() {
        throw new RuntimeException("Currently not supported"); //todo: implement this
    }

    @Override
    public String getGamemodeName() {
        return net.gtaun.shoebill.object.Server.get().getGamemodeText();
    }

    @Override
    public int getUpdateInterval() {
        return TICK; //todo: make this configurable?
    }

    @Override
    public PlusScheduler getScheduler() {
        return scheduler;
    }

    protected void destroy() {
        running = false;
        scheduler.cancelAllTasks();
        instance = null;
    }
}
