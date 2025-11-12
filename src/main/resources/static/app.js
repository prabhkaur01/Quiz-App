const API = {
  start: () => '/quiz/start',
  submit: () => '/quiz/submit',
  leaderboard: () => '/api/leaderboard'
};

let state = {
  questions: [],
  idx: 0,
  answers: new Map(), // qId -> selectedIndex
  username: '',
  startAt: 0,
  score: 0,
  streak: 0,
  timerInterval: null,
  quizSubmitted: false
};

const $ = sel => document.querySelector(sel);
const listEl = $('#questionList');
const qTitle = $('#qTitle');
const qDesc = $('#qDesc');
const choicesEl = $('#choices');
const feedback = $('#feedback');
const panel = $('#questionPanel');
const welcome = $('#welcome');

function fmt(ms) {
  const s = Math.floor(ms / 1000);
  const mm = String(Math.floor(s / 60)).padStart(2, '0');
  const ss = String(s % 60).padStart(2, '0');
  return `${mm}:${ss}`;
}

async function fetchLeaderboard() {
  const data = [
    { username: 'Tanmay', score: 10, correct: 5, totalQuestions: 5 },
    { username: 'Aditya', score: 8, correct: 4, totalQuestions: 5 },
    { username: 'Keshav', score: 6, correct: 3, totalQuestions: 5 },
    { username: state.username || 'You', score: state.score, correct: Math.floor(state.score / 2), totalQuestions: 5 }
  ];

  const lb = $('#leaderboard');
  lb.innerHTML = '';
  data.forEach(item => {
    const li = document.createElement('li');
    li.textContent = `${item.username} — ${item.score} pts (${item.correct}/${item.totalQuestions})`;
    lb.appendChild(li);
  });
}

function renderList() {
  listEl.innerHTML = '';
  state.questions.forEach((q, i) => {
    const li = document.createElement('li');
    li.textContent = `${i + 1}. ${q.title}`;
    li.classList.toggle('active', i === state.idx);
    if (state.answers.has(q.id)) {
      li.classList.add('answered');
      const correctIndex = q.__correct;
      if (correctIndex !== undefined) {
        li.classList.toggle('correct', state.answers.get(q.id) === correctIndex);
        li.classList.toggle('incorrect', state.answers.get(q.id) !== correctIndex);
      }
    }
    li.onclick = () => { if (!state.quizSubmitted) { state.idx = i; renderQuestion(); renderList(); } };
    listEl.appendChild(li);
  });
}

function renderQuestion() {
  const q = state.questions[state.idx];
  qTitle.textContent = q.title;
  qDesc.textContent = q.description;
  feedback.textContent = '';

  choicesEl.innerHTML = '';
  q.choices.forEach((ch, idx) => {
    const div = document.createElement('div');
    div.className = 'choice';
    div.textContent = ch;
    if (state.answers.get(q.id) === idx) div.classList.add('selected');
    if (!state.quizSubmitted) {
      div.onclick = () => {
        state.answers.set(q.id, idx);
        renderQuestion(); renderList();
      };
    }
    choicesEl.appendChild(div);
  });

  $('#prevBtn').disabled = state.idx === 0 || state.quizSubmitted;
  $('#nextBtn').disabled = state.idx === state.questions.length - 1 || state.quizSubmitted;
}

function applyInstantFeedback(correctIndex, selectedIndex) {
  const kids = Array.from(choicesEl.children);
  kids.forEach((el, i) => {
    el.classList.remove('correct', 'incorrect', 'selected');
    if (i === correctIndex) el.classList.add('correct');
    if (i === selectedIndex && i !== correctIndex) el.classList.add('incorrect');
  });
  feedback.textContent = (correctIndex === selectedIndex) ? 'Correct!' : `Wrong.`;
  feedback.className = 'feedback ' + ((correctIndex === selectedIndex) ? 'ok' : 'bad');
}

function startTimer() {
  const start = Date.now();
  state.startAt = start;
  clearInterval(state.timerInterval);
  state.timerInterval = setInterval(() => {
    $('#timer').textContent = fmt(Date.now() - start);
  }, 300);
}

$('#startBtn').onclick = async () => {
  clearInterval(state.timerInterval);
  state.quizSubmitted = false;
  state.username = $('#username').value.trim() || 'Anonymous';
  const topic = $('#topic').value;
  const diff = $('#difficulty').value ? Number($('#difficulty').value) : null;
  const body = { username: state.username, topic, limit: 10, difficulty: diff };

  const res = await fetch(API.start(), { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
  const data = await res.json();
  state.questions = data.questions;
  state.idx = 0;
  state.answers.clear();
  state.score = 0; state.streak = 0;
  $('#score').textContent = '0'; $('#streak').textContent = '0';

  welcome.classList.add('hidden'); panel.classList.remove('hidden');
  renderList(); renderQuestion(); startTimer(); fetchLeaderboard();
};

$('#prevBtn').onclick = () => { if (!state.quizSubmitted && state.idx > 0) { state.idx--; renderQuestion(); renderList(); } };
$('#nextBtn').onclick = () => { if (!state.quizSubmitted && state.idx < state.questions.length - 1) { state.idx++; renderQuestion(); renderList(); } };

$('#submitBtn').onclick = async () => {
  clearInterval(state.timerInterval);
  state.quizSubmitted = true;

  // hide buttons
  $('#prevBtn').style.display = 'none';
  $('#nextBtn').style.display = 'none';
  $('#submitBtn').style.display = 'none';

  // disable all choices
  choicesEl.querySelectorAll('.choice').forEach(c => c.onclick = null);

  const answers = state.questions.map(q => ({
    questionId: q.id,
    selectedIndex: state.answers.has(q.id) ? state.answers.get(q.id) : -1
  }));
  const durationMs = Date.now() - state.startAt;

  const res = await fetch(API.submit(), {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username: state.username, sessionStartedAt: state.startAt, answers, durationMs })
  });
  const data = await res.json();

  // feedback for all questions
  data.results.forEach(r => {
    const q = state.questions.find(x => x.id === r.questionId);
    if (!q) return;
    q.__correct = r.correctIndex;
    const selectedIndex = state.answers.get(q.id);
    if (selectedIndex !== undefined) applyInstantFeedback(r.correctIndex, selectedIndex);
  });

  // update score
  let streak = 0, best = 0, score = 0;
  data.results.forEach(r => {
    if (r.correct) { streak++; score += 2; if (streak > best) best = streak; } else streak = 0;
  });
  state.score = score;
  $('#score').textContent = String(score);
  $('#streak').textContent = String(best);

  renderList();
  await fetchLeaderboard();
  alert(`Quiz Finished!\nScore: ${data.score}\nCorrect: ${data.correct}/${data.totalQuestions}`);
};

// instant feedback only after submit
choicesEl.addEventListener('click', (e) => {
  if (!state.quizSubmitted) return;
  const q = state.questions[state.idx];
  if (!q || q.__correct === undefined) return;
  const selectedIndex = state.answers.get(q.id);
  if (selectedIndex === undefined) return;
  applyInstantFeedback(q.__correct, selectedIndex);
});

// load leaderboard
fetchLeaderboard().catch(() => { });
