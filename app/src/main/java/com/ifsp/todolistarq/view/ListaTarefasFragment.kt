package com.ifsp.todolistarq.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ifsp.todolistarq.R
import com.ifsp.todolistarq.databinding.FragmentListaTarefasBinding
import com.ifsp.todolistarq.model.entity.Tarefa
import com.ifsp.todolistarq.view.BaseFragment.Constantes.ACAO_TAREFA_EXTRA
import com.ifsp.todolistarq.view.BaseFragment.Constantes.CONSULTA
import com.ifsp.todolistarq.view.BaseFragment.Constantes.TAREFA_EXTRA
import com.ifsp.todolistarq.view.BaseFragment.Constantes.TAREFA_REQUEST_KEY
import com.ifsp.todolistarq.view.adapter.OnTarefaClickListener
import com.ifsp.todolistarq.view.adapter.TarefasAdapter
import com.ifsp.todolistarq.viewmodel.TarefaViewModel

class ListaTarefasFragment : BaseFragment(), OnTarefaClickListener {
    private lateinit var fragmentListaTarefasBinding: FragmentListaTarefasBinding
    private lateinit var tarefasList: MutableList<Tarefa>
    private lateinit var tarefasAdapter: TarefasAdapter

    //private lateinit var listaTarefasController: ListaTarefasController
    //private lateinit var listaTarefasController: TarefaPresenter
    private lateinit var tarefaViewModel: TarefaViewModel

    companion object {
        val ACTION_BUSCAR = "ACTION_BUSCAR"
        val EXTRA_BUSCAR = "BUSCAR"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //tarefaViewModel = TarefaPresenter(this)
        tarefaViewModel = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(TarefaViewModel::class.java)

        tarefaViewModel.recuperarListaTarefas().observe(this) { listaTarefas ->
            atualizarListaTarefas(listaTarefas)
        }


        setFragmentResultListener(TAREFA_REQUEST_KEY) { chave, resultados ->
            val tarefaExtra = resultados.getParcelable<Tarefa>(TAREFA_EXTRA)
            //adiciona ou atualiza uma tarefa da lista
            if (tarefaExtra != null) {
                var novaTarefa = true
                tarefasList.forEachIndexed { posicao, tarefa ->
                    if (tarefa.id == tarefaExtra.id) {
                        tarefasList.set(posicao, tarefaExtra)
                        novaTarefa = false
                    }
                }
                if (novaTarefa) {
                    tarefasList.add(tarefaExtra)
                }
                tarefasAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentListaTarefasBinding =
            FragmentListaTarefasBinding.inflate(inflater, container, false)
        //busca tarefas no banco de dados

        tarefasList = mutableListOf()
        tarefasAdapter = TarefasAdapter(this, tarefasList)
        val tarefasLayoutManager = LinearLayoutManager(activity)
        fragmentListaTarefasBinding.tarefasRv.adapter = tarefasAdapter
        fragmentListaTarefasBinding.tarefasRv.layoutManager = tarefasLayoutManager

        tarefaViewModel.buscarTarefas()

        return fragmentListaTarefasBinding.root
    }

    override fun onTarefaClick(posicao: Int) {
        //abre TarefaFragment para consulta de tarefa
        val tarefa = tarefasList[posicao]
        abreTarefaFragment(tarefa, true)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = tarefasAdapter.posicao
        return when (item.itemId) {
            R.id.editarTarefaMi -> {
                //abre TarefaFragment para edi????o de tarefa
                abreTarefaFragment(tarefasList[posicao], false)
                true
            }
            R.id.removerTarefaMi -> {
                tarefaViewModel.removerTarefa(tarefasList[posicao])
//                // Remove da lista de tarefas
                tarefasList.removeAt(posicao)
                tarefasAdapter.notifyDataSetChanged()
                true
            }
            else -> false
        }
    }

    private fun abreTarefaFragment(tarefa: Tarefa, consulta: Boolean) {
        //preparando tarefa para enviar para o TarefaFragment
        val argumentos = Bundle().also { bundle ->
            bundle.putParcelable(TAREFA_EXTRA, tarefa)
            if (consulta) {
                bundle.putInt(ACAO_TAREFA_EXTRA, CONSULTA)
            }
        }
        val tarefaFragment = TarefaFragment()
        tarefaFragment.arguments = argumentos

        activity?.supportFragmentManager?.commit {
            setReorderingAllowed(true)
            addToBackStack("TarefaFragment")
            replace(R.id.principalFcv, tarefaFragment)
        }
    }

    private val receiveBuscarTarefasBr: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val bundle = intent?.extras
                val lista = bundle?.getParcelableArray(EXTRA_BUSCAR)
                val listaTarefas: MutableList<Tarefa> = mutableListOf()
                lista?.forEach { item ->
                    listaTarefas.add(item as Tarefa)
                }

                atualizarListaTarefas(listaTarefas)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(
            receiveBuscarTarefasBr, IntentFilter(
                ACTION_BUSCAR
            )
        )
    }

    override fun atualizarListaTarefas(listaTarefas: MutableList<Tarefa>) {
        tarefasList.clear()
        tarefasList.addAll(listaTarefas)
        tarefasAdapter.notifyDataSetChanged()

    }

}