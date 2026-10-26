package com.example.receptbongeszo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private final List<Recipe> all=new ArrayList<>();
    private String category = "Mind";
    private String query = "";
    private RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        all.add(new Recipe("Bolognai", "Főétel", "https://thumb.wikimedia.org/wikipedia/commons/thumb/4/4d/Spaghetti_bolognese_%28hozinja%29_%28cropped%29.jpg/960px-Spaghetti_bolognese_%28hozinja%29_%28cropped%29.jpg", "A bolognai az olaszországi Bolognából származó húsos paradicsomszószos tészta. Az eredeti ragú alla bolognese-t hagyományosan tagliatellével tálalják."));
        all.add(new Recipe("Gulyás leves", "Leves", "https://upload.wikimedia.org/wikipedia/commons/2/2f/Goulash_hungarian.jpg?utm_source=commons.wikimedia.org&utm_campaign=imageinfo&utm_content=thumbnail_unscaled", "Magyar étel, amit eredetileg a pásztorok (gulyások) főztek bográcsban. Marhahúsból, hagymából, paprikából és burgonyából készül."));
        all.add(new Recipe("Palacsinta", "Desszert", "https://thumb.wikimedia.org/wikipedia/commons/thumb/7/75/Palacsinta.jpg/1280px-Palacsinta.jpg?utm_source=commons.wikimedia.org&utm_campaign=imageinfo&utm_content=thumbnail", "Vékony, serpenyőben sütött tészta, amit lekvárral, kakaóval, fahéjjal, túróval vagy Nutellával töltenek. Közép-Európában, így Magyarországon is nagyon népszerű."));

        adapter = new RecipeAdapter(new ArrayList<>(all));
        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nt) {
                query = nt == null ? "" : nt;
                filter();
                return true;
            }
        });

        setCategoryButton(R.id.btnALL, "Mind");
        setCategoryButton(R.id.btnSOUP, "Leves");
        setCategoryButton(R.id.btnMAIN, "Főétel");
        setCategoryButton(R.id.btnDESSERT, "Desszert");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> Toast.makeText(this, "Új recept", Toast.LENGTH_SHORT).show());

    }

    private void setCategoryButton(int id, String cat)
    {
        findViewById(id).setOnClickListener(v ->{category = cat; filter();});
    }

    private void filter()
    {
        List<Recipe> result = new ArrayList<>();
        for (Recipe r : all)
        {
            boolean catOK = category.equals("Mind") || r.getCategory().equals(category);
            boolean nameOK = r.getName().toLowerCase().contains(query.toLowerCase());
            if (catOK && nameOK)
            {
                result.add(r);
            }
        }
        adapter.update(result);
    }
}