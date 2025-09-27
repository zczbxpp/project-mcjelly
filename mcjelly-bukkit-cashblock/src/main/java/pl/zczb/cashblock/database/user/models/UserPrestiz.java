package pl.zczb.cashblock.database.user.models;


import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import pl.zczb.cashblock.database.codec.CodecHelper;
import pl.zczb.cashblock.database.codec.Converter;


public class UserPrestiz {

    @Setter
    @Getter
    private int prestiz;

    @Setter
    @Getter
    private int punktyPrestizu;

    @Setter
    @Getter
    private boolean autoPrestiz;


    @Setter
    @Getter
    private int moreExp;

    @Setter
    @Getter
    private int moreVpln;

    @Setter
    @Getter
    private int moreSpeed;

    @Setter
    @Getter
    private int mnoznikVpln;

    private UserPrestiz(int prestiz, int punktyPrestizu, boolean autoPrestiz, int moreExp, int moreVpln, int moreSpeed, int mnoznikVpln) {
        this.prestiz = prestiz;
        this.punktyPrestizu = punktyPrestizu;
        this.autoPrestiz = autoPrestiz;
        this.moreExp = moreExp;
        this.moreVpln = moreVpln;
        this.moreSpeed = moreSpeed;
        this.mnoznikVpln = mnoznikVpln;
    }

    public int addMnoznikVpln(final int mnoznikVpln) {
        this.mnoznikVpln += mnoznikVpln;
        return mnoznikVpln;
    }

    public int setMnoznikVpln(final int mnoznikVpln) {
        this.mnoznikVpln = mnoznikVpln;
        return mnoznikVpln;
    }


    public int addMoreSpeed(final int moreSpeed) {
        this.moreSpeed += moreSpeed;
        return moreSpeed;
    }

    public int setMoreSpeed(final int moreSpeed) {
        this.moreSpeed = moreSpeed;
        return moreSpeed;
    }


    public int addMoreVpln(final int moreVpln) {
        this.moreVpln += moreVpln;
        return moreVpln;
    }

    public int setMoreVpln(final int moreVpln) {
        this.moreVpln = moreVpln;
        return moreVpln;
    }


    public int addMoreExp(final int moreExp) {
        this.moreExp += moreExp;
        return moreExp;
    }

    public int setMoreExp(final int moreExp) {
        this.moreExp = moreExp;
        return moreExp;
    }


    public int addPrestiz(final int prestiz) {
        this.prestiz += prestiz;
        return prestiz;
    }

    public int addPunktyPrestizu(final int punktyPrestizu) {
        this.punktyPrestizu += punktyPrestizu;
        return punktyPrestizu;
    }

    public int removePunktyPrestizu(final int punktyPrestizu) {
        this.punktyPrestizu -= punktyPrestizu;
        return punktyPrestizu;
    }


    public static UserPrestiz createDefault() {
        return new UserPrestiz(0, 0, false, 0, 0, 0, 0);
    }

    public static class UserPrestizConverter implements Converter<UserPrestiz> {

        @Override
        public Document encode(UserPrestiz userSynchro) {
            Document document = new Document();
            document.put("prestiz", userSynchro.prestiz);
            document.put("punktyPrestizu", userSynchro.punktyPrestizu);
            document.put("autoPrestiz", userSynchro.autoPrestiz);
            document.put("moreExp", userSynchro.moreExp);
            document.put("moreVpln", userSynchro.moreVpln);
            document.put("moreSpeed", userSynchro.moreSpeed);
            document.put("mnoznikVpln", userSynchro.mnoznikVpln);
            return document;
        }

        @Override
        public UserPrestiz decode(Document document, CodecHelper helper) {
            return new UserPrestiz(
                    document.getInteger("prestiz"),
                    document.getInteger("punktyPrestizu"),
                    document.getBoolean("autoPrestiz"),
                    document.getInteger("moreExp"),
                    document.getInteger("moreVpln"),
                    document.getInteger("moreSpeed"),
                    document.getInteger("mnoznikVpln")


            );
        }

        @Override
        public Class<UserPrestiz> getConvertedClass() {
            return UserPrestiz.class;
        }
    }
}