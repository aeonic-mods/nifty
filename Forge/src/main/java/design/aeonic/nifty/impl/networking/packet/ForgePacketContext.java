package design.aeonic.nifty.impl.networking.packet;

public abstract class ForgePacketContext<T> {
    private final String id;
    private final T packet;

    public ForgePacketContext(String id, T packet) {
        this.id = id;
        this.packet = packet;
    }

    public String getId() {
        return id;
    }

    public T getPacket() {
        return packet;
    }

    public static class ServerToClient<T> extends ForgePacketContext<T> {
        public ServerToClient(String id, T packet) {
            super(id, packet);
        }

        @SuppressWarnings("unchecked")
        public static <T> Class<ServerToClient<T>> getPacketClass() {
            return (Class<ServerToClient<T>>) (Class<?>) ServerToClient.class;
        }
    }

    public static class ClientToServer<T> extends ForgePacketContext<T> {
        public ClientToServer(String id, T packet) {
            super(id, packet);
        }

        @SuppressWarnings("unchecked")
        public static <T> Class<ClientToServer<T>> getPacketClass() {
            return (Class<ClientToServer<T>>) (Class<?>) ClientToServer.class;
        }
    }
}
