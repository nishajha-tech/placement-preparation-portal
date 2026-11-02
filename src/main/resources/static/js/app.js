/**
 * Placement Preparation Portal - Core Frontend Application Script
 * Handles Aptitude verification, Mock Test Timer & Palette, Code Editor Runner, and Dynamic Eligibility.
 */

document.addEventListener('DOMContentLoaded', () => {

    // ===================================================================
    // 1. APTITUDE PRACTICE INTERACTIVE ANSWER CHECKER
    // ===================================================================
    const checkButtons = document.querySelectorAll('.btn-check-answer');
    checkButtons.forEach(btn => {
        btn.addEventListener('click', async (e) => {
            const card = e.target.closest('.question-card');
            const qId = card.getAttribute('data-question-id');
            const selectedRadio = card.querySelector(`input[name="opt_${qId}"]:checked`);
            const feedbackEl = card.querySelector('.answer-feedback');
            const explanationBox = card.querySelector('.explanation-box');

            if (!selectedRadio) {
                alert('Please select an option first before checking the answer!');
                return;
            }

            btn.disabled = true;
            btn.innerHTML = 'Checking...';

            try {
                const formData = new URLSearchParams();
                formData.append('questionId', qId);
                formData.append('selectedOption', selectedRadio.value);

                const response = await fetch('/aptitude/check-answer', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: formData
                });

                if (!response.ok) throw new Error('Network response error');
                const data = await response.json();

                // Style options
                const allLabels = card.querySelectorAll('.option-label');
                allLabels.forEach(label => {
                    const val = label.getAttribute('data-opt');
                    if (val === data.correctOption) {
                        label.classList.add('correct');
                    } else if (val === data.selectedOption && !data.isCorrect) {
                        label.classList.add('wrong');
                    }
                });

                // Display explanation
                if (explanationBox) {
                    explanationBox.classList.add('show');
                }

                btn.style.display = 'none';
            } catch (err) {
                console.error(err);
                btn.disabled = false;
                btn.innerHTML = 'Check Answer';
                alert('Failed to evaluate answer. Please try again.');
            }
        });
    });

    // ===================================================================
    // 2. TIMED MOCK TEST ENVIRONMENT
    // ===================================================================
    const testForm = document.getElementById('mockTestForm');
    if (testForm) {
        let totalSeconds = parseInt(testForm.getAttribute('data-duration') || '1200', 10);
        let timeElapsed = 0;
        const timerDisplay = document.getElementById('timerDisplay');
        const timeTakenInput = document.getElementById('timeTakenSecondsInput');
        const questionCards = document.querySelectorAll('.test-question-item');
        const paletteButtons = document.querySelectorAll('.palette-btn');
        let currentIndex = 0;

        // Show first question
        showQuestion(currentIndex);

        // Timer Interval
        const timerInterval = setInterval(() => {
            timeElapsed++;
            totalSeconds--;
            if (timeTakenInput) timeTakenInput.value = timeElapsed;

            if (totalSeconds <= 0) {
                clearInterval(timerInterval);
                alert('Time is up! Your mock test will now be automatically submitted.');
                testForm.submit();
                return;
            }

            const mins = Math.floor(totalSeconds / 60);
            const secs = totalSeconds % 60;
            if (timerDisplay) {
                timerDisplay.textContent = `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
                if (totalSeconds < 300) { // < 5 mins
                    timerDisplay.parentElement.classList.add('warning');
                }
            }
        }, 1000);

        function showQuestion(idx) {
            questionCards.forEach((card, i) => {
                card.style.display = (i === idx) ? 'block' : 'none';
            });
            paletteButtons.forEach((btn, i) => {
                btn.classList.toggle('current', i === idx);
            });
            currentIndex = idx;
        }

        // Palette Button Clicks
        paletteButtons.forEach(btn => {
            btn.addEventListener('click', () => {
                const targetIdx = parseInt(btn.getAttribute('data-index'), 10);
                showQuestion(targetIdx);
            });
        });

        // Next / Prev Buttons
        const prevBtn = document.getElementById('prevQuestionBtn');
        const nextBtn = document.getElementById('nextQuestionBtn');

        if (prevBtn) {
            prevBtn.addEventListener('click', () => {
                if (currentIndex > 0) showQuestion(currentIndex - 1);
            });
        }

        if (nextBtn) {
            nextBtn.addEventListener('click', () => {
                if (currentIndex < questionCards.length - 1) showQuestion(currentIndex + 1);
            });
        }

        // Update palette status on radio change
        testForm.addEventListener('change', (e) => {
            if (e.target.type === 'radio') {
                const currentPalBtn = paletteButtons[currentIndex];
                if (currentPalBtn) {
                    currentPalBtn.classList.remove('unanswered');
                    currentPalBtn.classList.add('answered');
                }
            }
        });

        // Mark for Review
        const reviewBtn = document.getElementById('markReviewBtn');
        if (reviewBtn) {
            reviewBtn.addEventListener('click', () => {
                const currentPalBtn = paletteButtons[currentIndex];
                if (currentPalBtn) {
                    currentPalBtn.classList.toggle('review');
                }
            });
        }

        // Clear Selection
        const clearBtn = document.getElementById('clearSelectionBtn');
        if (clearBtn) {
            clearBtn.addEventListener('click', () => {
                const currentCard = questionCards[currentIndex];
                const checkedRadio = currentCard.querySelector('input[type="radio"]:checked');
                if (checkedRadio) {
                    checkedRadio.checked = false;
                    const currentPalBtn = paletteButtons[currentIndex];
                    if (currentPalBtn) {
                        currentPalBtn.classList.remove('answered');
                        currentPalBtn.classList.add('unanswered');
                    }
                }
            });
        }

        // Submit Button with confirmation summary
        const submitBtn = document.getElementById('submitTestBtn');
        if (submitBtn) {
            submitBtn.addEventListener('click', () => {
                let answeredCount = 0;
                questionCards.forEach(card => {
                    if (card.querySelector('input[type="radio"]:checked')) answeredCount++;
                });
                const total = questionCards.length;
                const unanswered = total - answeredCount;

                const confirmed = confirm(
                    `Test Submission Summary:\n\n• Answered: ${answeredCount}\n• Unattempted: ${unanswered}\n• Total Questions: ${total}\n\nAre you sure you want to finish and submit the test?`
                );
                if (confirmed) {
                    clearInterval(timerInterval);
                    testForm.submit();
                }
            });
        }
    }

    // ===================================================================
    // 3. JAVA CODING RUNNER & SUBMISSION
    // ===================================================================
    const runCodeBtn = document.getElementById('runCodeBtn');
    const submitCodeBtn = document.getElementById('submitCodeBtn');
    const resetCodeBtn = document.getElementById('resetCodeBtn');
    const codeEditor = document.getElementById('sourceCodeEditor');
    const consoleOutput = document.getElementById('consoleOutput');
    const testCasesContainer = document.getElementById('testCasesContainer');

    if (runCodeBtn && codeEditor) {
        const problemId = runCodeBtn.getAttribute('data-problem-id');
        const defaultCode = codeEditor.value;

        if (resetCodeBtn) {
            resetCodeBtn.addEventListener('click', () => {
                if (confirm('Reset code editor to initial starter template?')) {
                    codeEditor.value = defaultCode;
                }
            });
        }

        runCodeBtn.addEventListener('click', async () => {
            runCodeBtn.disabled = true;
            runCodeBtn.innerHTML = '⚙️ Compiling & Running...';
            consoleOutput.innerHTML = '<span style="color:#38bdf8;">Compiling Java code and testing sample cases...</span>';

            try {
                const res = await fetch('/coding/run', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        problemId: problemId,
                        sourceCode: codeEditor.value
                    })
                });
                const data = await res.json();
                renderExecutionResult(data);
            } catch (err) {
                consoleOutput.innerHTML = '<span style="color:#ef4444;">Error executing code: ' + err.message + '</span>';
            } finally {
                runCodeBtn.disabled = false;
                runCodeBtn.innerHTML = '▶ Run Code';
            }
        });

        if (submitCodeBtn) {
            submitCodeBtn.addEventListener('click', async () => {
                submitCodeBtn.disabled = true;
                submitCodeBtn.innerHTML = '🚀 Evaluating...';
                consoleOutput.innerHTML = '<span style="color:#38bdf8;">Running all test cases for final submission...</span>';

                try {
                    const res = await fetch('/coding/submit', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            problemId: problemId,
                            sourceCode: codeEditor.value
                        })
                    });
                    const data = await res.json();
                    
                    if (data.status === 'ACCEPTED') {
                        consoleOutput.innerHTML = `
                            <div style="padding:12px; border-radius:8px; background:#064e3b; color:#a7f3d0; margin-bottom:10px;">
                                <strong>🎉 ACCEPTED!</strong> All ${data.passed}/${data.total} test cases passed.<br>
                                Problem solved status recorded on your profile.
                            </div>
                            <pre style="color:#d4d4d4;">${data.output || ''}</pre>
                        `;
                    } else {
                        consoleOutput.innerHTML = `
                            <div style="padding:12px; border-radius:8px; background:#7f1d1d; color:#fecaca; margin-bottom:10px;">
                                <strong>❌ ${data.status}</strong> (${data.passed}/${data.total} test cases passed).
                            </div>
                            <pre style="color:#f87171;">${data.output || ''}</pre>
                        `;
                    }
                } catch (err) {
                    consoleOutput.innerHTML = '<span style="color:#ef4444;">Error: ' + err.message + '</span>';
                } finally {
                    submitCodeBtn.disabled = false;
                    submitCodeBtn.innerHTML = 'Submit Solution';
                }
            });
        }

        function renderExecutionResult(result) {
            let html = '';
            const isAccepted = (result.status === 'ACCEPTED');
            const badgeBg = isAccepted ? '#10b981' : (result.status === 'COMPILATION_ERROR' ? '#f59e0b' : '#ef4444');

            html += `<div style="display:flex; align-items:center; justify-content:space-between; margin-bottom:12px;">
                        <div>Status: <span style="background:${badgeBg}; color:#fff; padding:3px 8px; border-radius:4px; font-weight:700;">${result.status}</span></div>
                        <div>Passed: <strong>${result.passedCount} / ${result.totalCount}</strong></div>
                     </div>`;

            if (result.message) {
                html += `<div style="margin-bottom:14px; color:${isAccepted ? '#34d399' : '#f87171'}; white-space:pre-wrap;">${result.message}</div>`;
            }

            if (result.testCaseResults && result.testCaseResults.length > 0) {
                html += '<div style="margin-top:10px;"><strong>Test Cases Details:</strong></div>';
                result.testCaseResults.forEach((tc, i) => {
                    const statusColor = tc.passed ? '#34d399' : '#f87171';
                    const icon = tc.passed ? '✓ Passed' : '✗ Failed';
                    html += `
                        <div style="background:#27272a; padding:10px 14px; border-radius:6px; margin-top:8px; border-left:4px solid ${statusColor};">
                            <div style="display:flex; justify-content:space-between;">
                                <span>Case #${i + 1}</span>
                                <span style="color:${statusColor}; font-weight:700;">${icon}</span>
                            </div>
                            <div style="font-size:0.82rem; margin-top:4px; color:#a1a1aa;">Input: <code>${tc.input}</code></div>
                            <div style="font-size:0.82rem; color:#a1a1aa;">Expected: <code>${tc.expectedOutput}</code></div>
                            <div style="font-size:0.82rem; color:${statusColor};">Your Output: <code>${tc.actualOutput}</code></div>
                        </div>
                    `;
                });
            }

            consoleOutput.innerHTML = html;
        }
    }
});
