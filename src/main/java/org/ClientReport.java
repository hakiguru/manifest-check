package org;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import java.util.ArrayList;
import java.util.List;

public record ClientReport(int protocol, String packId, String packVersion, String loader, String loaderVersion, String mcVersion, List<Mod> mods, int cheatFlags) {

    public record Mod(String id, String version) {}

    private static final  int MAX_BYTES = 64 * 1024;
    private static final int MAX_MODS = 1000;

    public static ClientReport parse(byte[] data, long expectedNonce) {
        if (data.length > MAX_BYTES) {
            return null;
        }
        try {
            ByteArrayDataInput in = ByteStreams.newDataInput(data);

            int protocol = in.readInt();
            if (protocol != 1) {
                return null;
            }
            long nonce = in.readLong();
            if (nonce != expectedNonce) {
                return null;
            }
            String packId = in.readUTF(); //packId
            String packVersion = in.readUTF(); //packVersion
            String loader = in.readUTF(); //loader
            String loaderVersion = in.readUTF(); //loaderVersion
            String mcVersion = in.readUTF(); //mcVersion

            //use only this order

            int count = in.readInt();
            if (count < 0 || count > MAX_MODS) {
                return null;
            }

            List<Mod> mods = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                mods.add(new Mod(in.readUTF(), in.readUTF()));
            }

            int cheatFlags = in.readInt();

            return new ClientReport(protocol, packId, packVersion, loader, loaderVersion, mcVersion, List.copyOf(mods), cheatFlags);
        } catch (IllegalStateException e) {
            return null;
        }
    }
}
