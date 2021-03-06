package ru.mobiledevelopment.powertodo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import ru.mobiledevelopment.powertodo.databinding.ActivityMainBinding
import ru.mobiledevelopment.powertodo.ui.main.*
import ru.mobiledevelopment.powertodo.ui.main.viewModel.ModelFactory
import ru.mobiledevelopment.powertodo.ui.main.viewModel.ToDoViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tabs.setupWithViewPager(binding.viewPager)

        val sectionsPagerAdapter =  SectionsPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter

    }
}