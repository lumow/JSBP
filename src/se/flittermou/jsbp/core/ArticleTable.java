package se.flittermou.jsbp.core;

import se.flittermou.jsbp.db.Column;
import se.flittermou.jsbp.db.DB;
import se.flittermou.jsbp.db.Table;
import se.flittermou.jsbp.db.datatypes.Date;
import se.flittermou.jsbp.db.datatypes.Int;
import se.flittermou.jsbp.db.datatypes.Real;
import se.flittermou.jsbp.db.datatypes.Str;
import se.flittermou.jsbp.db.datatypes.modifiers.NOTNULL;
import se.flittermou.jsbp.db.datatypes.modifiers.PRIMARY;

import java.util.ArrayList;
import java.util.List;

public class ArticleTable extends Table {
    private static final String TABLE_NAME = "article";
    private static List<Column> columns = new ArrayList<Column>() {
        {
            add(Column.create("Artikelid", new Int()).addModifier(new NOTNULL()).addModifier(new PRIMARY()));
            add(Column.create("nr", new Int()).addModifier(new NOTNULL()));
            add(Column.create("Varnummer", new Int()));
            add(Column.create("Namn", new Str()).addModifier(new NOTNULL()));
            add(Column.create("Namn2", new Str()));
            add(Column.create("Prisinklmoms", new Real()));
            add(Column.create("Volymiml", new Real()));
            add(Column.create("PrisPerLiter", new Real()));
            add(Column.create("Saljstart", new Date()));
            add(Column.create("Slutlev", new Str()));
            add(Column.create("Varugrupp", new Str()));
            add(Column.create("Forpackning", new Str()));
            add(Column.create("Forslutning", new Str()));
            add(Column.create("Ursprung", new Str()));
            add(Column.create("Ursprunglandnamn", new Str()));
            add(Column.create("Producent", new Str()));
            add(Column.create("Leverantor", new Str()));
            add(Column.create("Argang", new Date()));
            add(Column.create("Provadargang", new Date()));
            add(Column.create("Alkoholhalt", new Real()));
            add(Column.create("Modul", new Str()));
            add(Column.create("Sortiment", new Str()));
            add(Column.create("Ekologisk", new Int()));
            add(Column.create("Koscher", new Int()));
            add(Column.create("RavarorBeskrivning", new Str()));
            add(Column.create("Pant", new Real()));
        }
    };

    public ArticleTable(DB db) {
        super(db, TABLE_NAME, columns);
    }
}
