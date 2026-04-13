import java.util.*;

abstract class Member {
    protected String id;
    protected String nama;
    protected int saldo;

    public Member(String id, String nama, int saldo) {
        this.id = id;
        this.nama = nama;
        this.saldo = saldo;
    }

    public void topUp(int jumlah) {
        this.saldo += jumlah;
    }

    public int getSaldo() {
        return saldo;
    }

    public String getInfo() {
        return id + " | " + nama + " | " + getType() + " | saldo: " + saldo;
    }

    protected abstract String getType();

    public abstract int hitungPembayaran(int hargaDasar, int sesi);
}

class Regular extends Member {
    public Regular(String id, String nama) {
        super(id, nama, 0);
    }

    @Override
    protected String getType() {
        return "REGULER";
    }

    @Override
    public int hitungPembayaran(int hargaDasar, int sesi) {
        int subtotal = hargaDasar * sesi;

        if (sesi > 5) {
            subtotal -= (int) Math.floor(subtotal * 0.10);
        }

        int pajak = (int) Math.floor(subtotal * 0.05);
        int total = subtotal + pajak;

        return Math.max(total, 0);
    }
}

class VIP extends Member {
    public VIP(String id, String nama) {
        super(id, nama, 0);
    }

    @Override
    protected String getType() {
        return "VIP";
    }

    @Override
    public int hitungPembayaran(int hargaDasar, int sesi) {
        int subtotal = hargaDasar * sesi;

        if (sesi > 5) {
            subtotal -= (int) Math.floor(subtotal * 0.10);
        }

        subtotal -= (int) Math.floor(subtotal * 0.15);

        // Pajak 5% dari subtotal
        int pajak = (int) Math.floor(subtotal * 0.05);
        int total = subtotal + pajak;

        return Math.max(total, 0);
    }
}

class GymSystem {
    private List<Member> members = new ArrayList<>();

    public void addMember(String tipe, String id, String nama) {
        if (findMember(id) != null) {
            System.out.println("Member sudah terdaftar");
            return;
        }

        Member m;
        if (tipe.equals("REGULER")) {
            m = new Regular(id, nama);
        } else {
            m = new VIP(id, nama);
        }
        members.add(m);
        System.out.println(tipe + " " + id + " berhasil ditambahkan");
    }

    public void topUp(String id, int jumlah) {
        Member m = findMember(id);
        if (m == null) {
            System.out.println("Member tidak ditemukan");
            return;
        }
        m.topUp(jumlah);
        System.out.println("Saldo " + id + ": " + m.getSaldo());
    }

    public void buy(String id, String layanan, int sesi) {
        Member m = findMember(id);
        if (m == null) {
            System.out.println("Member tidak ditemukan");
            return;
        }

        int hargaDasar = getHargaLayanan(layanan);
        if (hargaDasar == -1) {
            System.out.println("Layanan tidak valid");
            return;
        }

        int total = m.hitungPembayaran(hargaDasar, sesi);

        if (m.getSaldo() < total) {
            System.out.println("Saldo " + id + " tidak cukup");
            return;
        }

        m.saldo -= total;
        System.out.println("Total bayar " + id + ": " + total);
        System.out.println("Saldo " + id + ": " + m.getSaldo());
    }

    public void check(String id) {
        Member m = findMember(id);
        if (m == null) {
            System.out.println("Member tidak ditemukan");
            return;
        }
        System.out.println(m.getInfo());
    }

    public int count() {
        return members.size();
    }

    public Member findMember(String id) {
        for (Member m : members) {
            if (m.id.equals(id)) return m;
        }
        return null;
    }

    private int getHargaLayanan(String layanan) {
        switch (layanan) {
            case "cardio": return 20000;
            case "yoga": return 25000;
            case "personal_training": return 40000;
            default: return -1;
        }
    }
}

public class GymSehatSelalu {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GymSystem gym = new GymSystem();

        int n = Integer.parseInt(sc.nextLine().trim());

        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            String[] parts = line.split(" ");
            String cmd = parts[0];

            switch (cmd) {
                case "ADD": {
                    String tipe = parts[1];
                    String id = parts[2];
                    String nama = parts[3];
                    gym.addMember(tipe, id, nama);
                    break;
                }
                case "TOPUP": {
                    String id = parts[1];
                    int jumlah = Integer.parseInt(parts[2]);
                    gym.topUp(id, jumlah);
                    break;
                }
                case "BUY": {
                    String id = parts[1];
                    String layanan = parts[2];
                    int sesi = Integer.parseInt(parts[3]);
                    gym.buy(id, layanan, sesi);
                    break;
                }
                case "CHECK": {
                    String id = parts[1];
                    gym.check(id);
                    break;
                }
                case "COUNT": {
                    System.out.println("Total member: " + gym.count());
                    break;
                }
                default:
                    System.out.println("Perintah tidak dikenal");
            }
        }

        sc.close();
    }
}

