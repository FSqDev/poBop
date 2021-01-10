package com.fsq.pobop.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.fsq.pobop.entity.ingredient.Ingredient;
import com.fsq.pobop.entity.ingredient.IngredientDao;

@Database(entities = {Ingredient.class}, version = 2)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract IngredientDao ingredientDao();

    private static AppDatabase databaseInstance;

    static final Migration MIGRATION_0_1 = new Migration(0, 1) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database){
            database.execSQL("CREATE TABLE ingredient (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT, expiry_date DATE)");
            database.execSQL("CREATE INDEX index_ingredient_id on ingredient (id)");
        }
    };

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP INDEX IF EXISTS index_ingredient_id");
            database.execSQL("DROP TABLE IF EXISTS ingredient");
            database.execSQL("CREATE TABLE ingredient (id VARCHAR(24) PRIMARY KEY NOT NULL, name TEXT, expiry_date DATE, barcode TEXT, product_type TEXT, image_url TEXT, dirty INTEGER NOT NULL DEFAULT 1 )");
            database.execSQL("CREATE INDEX index_ingredient_id ON ingredient(id)");
        }
    };

    public static synchronized AppDatabase getInstance(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database")
                    .addMigrations(
                            MIGRATION_0_1,
                            MIGRATION_1_2
                    )
                    .build();
        }
        return databaseInstance;
    }
}
