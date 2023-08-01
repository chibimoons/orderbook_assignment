package com.junyoung.ha.common.bloc

import com.junyoung.ha.common.bloc.Bloc
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class BlocTest {

    @Mock
    lateinit var testUseCase: LongWorkAndPreemptiveBlocTestSpec.TestUseCase

    private val actualState = mutableListOf<Any>()
    private var job: Job? = null

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @After
    fun tearDown() {
        actualState.clear()
        job = null
    }

    @Test
    fun ActionMapper_단순_순서_보장_테스트() = runBlockingTest {
        // GIVEN
        val actionTransformer = SimpleBlocTestSpec.ActionTransformer()
        val actionMapper = SimpleBlocTestSpec.ActionMapper()
        val errorMapper = SimpleBlocTestSpec.ErrorMapper()
        val bloc = Bloc(
            initialState = SimpleBlocTestSpec.State(),
            actionTransformer = actionTransformer,
            actionMapper = actionMapper,
            errorMapper = errorMapper,
            blocScope = TestScope(UnconfinedTestDispatcher())
        )
        bloc.start()

        val givenState = bloc.currentState
        val givenActions = listOf(
            SimpleBlocTestSpec.Action.A,
            SimpleBlocTestSpec.Action.B,
            SimpleBlocTestSpec.Action.C,
            SimpleBlocTestSpec.Action.D,
            SimpleBlocTestSpec.Action.E,
            SimpleBlocTestSpec.Action.F
        )

        // WHEN
        job = launch { bloc.stateFlow.toList(actualState) }
        givenActions.forEach { bloc.dispatch(it) }
        delay(20)
        job?.cancel()

        // THEN
        val expectStates = listOf(
            givenState,
            givenState.copy(
                message = SimpleBlocTestSpec.Action.A.javaClass.simpleName
            ),
            givenState.copy(
                message = SimpleBlocTestSpec.Action.A.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.B.javaClass.simpleName
            ),
            givenState.copy(
                message = SimpleBlocTestSpec.Action.A.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.B.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.C.javaClass.simpleName
            ),
            givenState.copy(
                message = SimpleBlocTestSpec.Action.A.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.B.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.C.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.D.javaClass.simpleName
            ),
            givenState.copy(
                message = SimpleBlocTestSpec.Action.A.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.B.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.C.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.D.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.E.javaClass.simpleName
            ),
            givenState.copy(
                message = SimpleBlocTestSpec.Action.A.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.B.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.C.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.D.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.E.javaClass.simpleName
                        + SimpleBlocTestSpec.Action.F.javaClass.simpleName
            ),
        )

        expectStates.indices.forEach {
            Assert.assertEquals(expectStates[it], actualState[it])
        }
        bloc.end()
    }

    @Test
    fun ActionMapper_작업_취소_후_동작_테스트() = runBlockingTest {
        // GIVEN
        val actionTransformer = CancelBlocTestSpec.ActionTransformer()
        val actionMapper = CancelBlocTestSpec.ActionMapper()
        val bloc = Bloc(
            initialState = CancelBlocTestSpec.State(),
            actionTransformer = actionTransformer,
            actionMapper = actionMapper,
            blocScope = TestScope(UnconfinedTestDispatcher())
        )
        bloc.start()

        val givenState = bloc.currentState

        // WHEN
        job = launch { bloc.stateFlow.toList(actualState) }
        bloc.dispatch(CancelBlocTestSpec.Action.LongWorkForCancelAction)
        delay(1000)
        bloc.cancelAllAction()
        bloc.dispatch(CancelBlocTestSpec.Action.NewWorkAction)
        delay(20)
        job?.cancel()

        // THEN
        val expectStates = listOf(
            givenState,
            givenState.copy(
                message = CancelBlocTestSpec.Action.NewWorkAction.javaClass.simpleName
            )
        )

        expectStates.indices.forEach {
            Assert.assertEquals(expectStates[it], actualState[it])
        }
        bloc.end()
    }

    @Test
    fun ActionMapper에서_긴작업을_진행중인상태에서_ActionTransformer의_Action_처리_테스트() = runBlockingTest {
        // GIVEN
        val actionTransformer = LongWorkAndPreemptiveBlocTestSpec.ActionTransformer(testUseCase)
        val actionMapper = LongWorkAndPreemptiveBlocTestSpec.ActionMapper()
        val bloc = Bloc(
            initialState = LongWorkAndPreemptiveBlocTestSpec.State(),
            actionTransformer = actionTransformer,
            actionMapper = actionMapper,
            blocScope = TestScope(UnconfinedTestDispatcher())
        )
        bloc.start()

        // WHEN
        bloc.dispatch(LongWorkAndPreemptiveBlocTestSpec.Action.LongWorkAction)
        delay(1000)
        bloc.dispatch(LongWorkAndPreemptiveBlocTestSpec.Action.PreemptiveAction)
        delay(20)

        // THEN
        verify(testUseCase, times(1)).workPreemptive()
        Assert.assertTrue(true)
        bloc.end()
    }
}