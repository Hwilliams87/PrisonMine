/*
 * BlocksUtil.java
 * 
 * PrisonMine
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.prison.mines.util;

import java.lang.reflect.Field;

import net.minecraft.server.v1_5_R2.ChunkSection;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class BlocksUtil {
    
    public static void setBlockSuperFast(Block b, int typeId, byte data) {
        Chunk c = b.getChunk();
        net.minecraft.server.v1_5_R2.Chunk chunk = ((org.bukkit.craftbukkit.v1_5_R2.CraftChunk) c).getHandle();
         
        try {
            Field f = chunk.getClass().getDeclaredField("sections");
            f.setAccessible(true);
            ChunkSection[] sections = (ChunkSection[]) f.get(chunk);
            ChunkSection chunksection = sections[b.getY() >> 4];
         
            if (chunksection == null) {
                if (typeId == 0) return;
                chunksection = sections[b.getY() >> 4] = new ChunkSection(b.getY() >> 4 << 4, !chunk.world.worldProvider.f);
            }
         
            chunksection.a(b.getX() & 15, b.getY() & 15, b.getZ() & 15, typeId);
            chunksection.b(b.getX() & 15, b.getY() & 15, b.getZ() & 15, data);
         
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    /**
     * Placeholder method. If it throws an error, server's CraftBukkit version differs from the one
     * the plugin was compiled with.
     */
    public static void isBukkitCompatible() { }
    
}
