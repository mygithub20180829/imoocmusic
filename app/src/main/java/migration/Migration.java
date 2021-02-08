package migration;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;
import models.MusicModel;

public class Migration implements RealmMigration {
    @Override
    //动态Realm对象，上一个数据库的版本默认值为0，最新的数据库版本
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        //告诉哪些发生迁移
        RealmSchema schema=realm.getSchema();
        /**
         * 第一次迁移
         */
        if (oldVersion==0){
            //schema.remove("MusicModel");//别人加的
            schema.create("MusicModel")
                    .addField("musicId",String.class)
                    .addField("name",String.class)
                    .addField("poster",String.class)
                    .addField("path",String.class)
                    .addField("author",String.class);
            schema.create("AlbumModel")
                    .addField("albumId",String.class)
                    .addField("name",String.class)
                    .addField("poster",String.class)
                    .addField("playNum",String.class)
                   .addRealmListField("list", schema.get("MusicModel"));

            schema.create("MusicSourceModel")
                    .addRealmListField("album",schema.get("AlbumModel"))
                    .addRealmListField("hot",schema.get("MusicModel"));
            oldVersion=newVersion;
        }
    }
}
