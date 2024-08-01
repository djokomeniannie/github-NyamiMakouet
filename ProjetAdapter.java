package NyamiMakouet;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjetAdapter extends TypeAdapter<Projet> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void write(JsonWriter out, Projet projet) throws IOException {
        out.beginObject();
        out.name("id").value(projet.getId());
        out.name("nom").value(projet.getNom());
        out.name("dateDebut").value(dateFormat.format(projet.getDateDebut()));
        out.name("dateFin").value(dateFormat.format(projet.getDateFin()));
        out.name("heuresPrevues").value(projet.getHeuresPrevues());
        out.name("disciplines").beginArray();
        for (Discipline discipline : projet.getDisciplines()) {
            out.value(discipline.getNom());
        }
        out.endArray();
        out.endObject();
    }

    @Override
    public Projet read(JsonReader in) throws IOException {
        in.beginObject();
        int id = 0;
        String nom = null;
        Date dateDebut = null;
        Date dateFin = null;
        int heuresPrevues = 0;
        List<Discipline> disciplines = new ArrayList<>();

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    id = in.nextInt();
                    break;
                case "nom":
                    nom = in.nextString();
                    break;
                case "dateDebut":
                    try {
                        dateDebut = dateFormat.parse(in.nextString());
                    } catch (ParseException e) {
                        throw new IOException(e);
                    }
                    break;
                case "dateFin":
                    try {
                        dateFin = dateFormat.parse(in.nextString());
                    } catch (ParseException e) {
                        throw new IOException(e);
                    }
                    break;
                case "heuresPrevues":
                    heuresPrevues = in.nextInt();
                    break;
                case "disciplines":
                    in.beginArray();
                    while (in.hasNext()) {
                        disciplines.add(new Discipline(in.nextString()));
                    }
                    in.endArray();
                    break;
            }
        }
        in.endObject();
        return new Projet(id, nom, dateDebut, dateFin, heuresPrevues, disciplines);
    }
}
