package com.example.oficina_android

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 * Fragment para a tela Sobre.
 */
class AboutFragment : Fragment(R.layout.fragment_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titles = resources.getStringArray(R.array.about_team_titles)
        val responsibilities = resources.getStringArray(R.array.about_team_responsibilities)
        val members = resources.getStringArray(R.array.about_team_members)
        val teamList = view.findViewById<LinearLayout>(R.id.aboutTeamList)

        titles.indices.forEach { index ->
            val teamCard = layoutInflater.inflate(R.layout.item_about_team, teamList, false)
            teamCard.findViewById<TextView>(R.id.tvTeamTitle).text = titles[index]
            teamCard.findViewById<TextView>(R.id.tvTeamResponsibility).text =
                getString(R.string.about_team_responsibility, responsibilities[index])
            teamCard.findViewById<TextView>(R.id.tvTeamMembers).text =
                getString(R.string.about_team_members, members[index])
            teamList.addView(teamCard)
        }
    }
}
