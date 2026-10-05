package org;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforgespi.language.IModInfo;

public final class HelloReport {
    public static final int PROTOCOL = 1;

    private HelloReport() {
    }

    public static byte[] build(long nonce) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeInt(PROTOCOL);
        out.writeLong(nonce);
        out.writeUTF(Config.PACK_ID.get());
        out.writeUTF(Config.PACK_VERSION.get());
        out.writeUTF("neoforge");
        out.writeUTF(FMLLoader.versionInfo().neoForgeVersion());
        out.writeUTF(FMLLoader.versionInfo().mcVersion());

        var mods = ModList.get().getMods();
        out.writeInt(mods.size());
        for (IModInfo mod : mods) {
            out.writeUTF(mod.getModId());
            out.writeUTF(mod.getVersion().toString());
        }

        out.writeInt(0);
        return out.toByteArray();
    }
}
