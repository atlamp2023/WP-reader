package com.github.atlamp2023.wpreader

import com.github.atlamp2023.wpreader.core.util.State
import com.github.atlamp2023.wpreader.features.list.data.ListRepository
import com.github.atlamp2023.wpreader.features.list.data.LocalListSource
import com.github.atlamp2023.wpreader.features.list.data.RemoteListSource
import com.github.atlamp2023.wpreader.features.list.domain.GetListUseCase
import com.github.atlamp2023.wpreader.features.list.presentation.ListItem
import kotlinx.coroutines.*
import org.junit.Test
import org.junit.Assert
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetListUseCaseTest {

    @Test
    fun `should return same data as in repository (state REMOTE)`(){
        val mockRepository = mock<ListRepository>()

        val testListReturn = listOf<ListItem>(
            ListItem(1, "test", "preview", "localhost"),
            ListItem(2, "test2", "preview2", "localhost")
        )
        Mockito.`when`(mockRepository.getListRemoteAsync()).thenReturn(
            CoroutineScope(Dispatchers.Unconfined).async {
                testListReturn
            }
        )

        val useCase = GetListUseCase(mockRepository)

        var actual: List<ListItem>? = null

        runBlocking {
            actual = useCase.execute(State.REMOTE)
        }

        val expected = listOf<ListItem>(
            ListItem(1, "test", "preview", "localhost"),
            ListItem(2, "test2", "preview2", "localhost")
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `should return same data as in source (state REMOTE)`() {
        val localSource = LocalListSource()
        val mockRemoteSource = mock<RemoteListSource>()
        val repository = ListRepository(mockRemoteSource, localSource)

        val testListReturn = listOf<ListItem>(
            ListItem(1, "test", "preview", "localhost"),
            ListItem(2, "test2", "preview2", "localhost")
        )
        Mockito.`when`(mockRemoteSource.getListRemote()).thenReturn(
            testListReturn
        )

        val useCase = GetListUseCase(repository)

        var actual: List<ListItem>? = null

        runBlocking {
            actual = useCase.execute(State.REMOTE)
        }

        val expected = listOf<ListItem>(
            ListItem(1, "test", "preview", "localhost"),
            ListItem(2, "test2", "preview2", "localhost")
        )

        Assert.assertEquals(expected, actual)
    }
}