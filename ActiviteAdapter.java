package NyamiMakouet;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActiviteAdapter extends TypeAdapter<Activite> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Override
    public void write(JsonWriter out, Activite activite) throws IOException {
        out.beginObject();
        out.name("employeId").value(activite.getEmployeId());
        out.name("projetId").value(activite.getProjetId());
        out.name("discipline").value(activite.getDiscipline());
        out.name("dateDebut").value(dateFormat.format(activite.getDateDebut()));
        if (activite.getDateFin() != null) {
            out.name("dateFin").value(dateFormat.format(activite.getDateFin()));
        } else {
            out.name("dateFin").nullValue();
        }
        out.endObject();
    }

    @Override
    public Activite read(JsonReader in) throws IOException {
        in.beginObject();
        int employeId = 0;
        int projetId = 0;
        String discipline = null;
        Date dateDebut = null;
        Date dateFin = null;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "employeId":
                    employeId = in.nextInt();
                    break;
                case "projetId":
                    projetId = in.nextInt();
                    break;
                case "discipline":
                    discipline = in.nextString();
                    break;
                case "dateDebut":
                    try {
                        dateDebut = dateFormat.parse(in.nextString());
                    } catch (ParseException e) {
                        throw new IOException(e);
                    }
                    break;
                case "dateFin":
                    String dateFinStr = in.nextString();
                    if (dateFinStr != null) {
                        try {
                            dateFin = dateFormat.parse(dateFinStr);
                        } catch (ParseException e) {
                            throw new IOException(e);
                        }
                    }
                    break;
            }
        }
        in.endObject();
        return new Activite(employeId, projetId, discipline, dateDebut, dateFin);
    }
}
