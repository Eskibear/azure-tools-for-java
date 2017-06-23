/**
 * Copyright (c) Microsoft Corporation
 *
 * All rights reserved.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.microsoft.azuretools.container.ui.wizard.publish;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.microsoft.azuretools.container.presenters.PublishWizardPresenter;
import com.microsoft.azuretools.core.components.AzureWizardPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.layout.FillLayout;

public class StepTwoPage extends AzureWizardPage {
    private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
    private Table tableWebApps;
	private final PublishWizardPresenter<AzureWizardPage> presenter;

    /**
     * Create the wizard.
     */
    public StepTwoPage() {
        super("wizardPage");
		presenter = new PublishWizardPresenter<AzureWizardPage>();
        presenter.onAttachView(this);
        
        setTitle("Deploy to Web App On Linux");
        setDescription("TBD");
    }

    /**
     * Create contents of the wizard.
     * @param parent
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        setControl(container);
        container.setLayout(new GridLayout(1, false));
        
        Composite cmpoWebAppOnLinux = formToolkit.createComposite(container, SWT.NONE);
        cmpoWebAppOnLinux.setLayout(new GridLayout(2, false));
        GridData gd_cmpoWebAppOnLinux = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_cmpoWebAppOnLinux.heightHint = 222;
        cmpoWebAppOnLinux.setLayoutData(gd_cmpoWebAppOnLinux);
        formToolkit.paintBordersFor(cmpoWebAppOnLinux);
        
        Composite cmpoWebAppsTable = formToolkit.createComposite(cmpoWebAppOnLinux, SWT.NONE);
        cmpoWebAppsTable.setLayout(new FillLayout(SWT.HORIZONTAL));
        GridData gd_cmpoWebAppsTable = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_cmpoWebAppsTable.widthHint = 394;
        cmpoWebAppsTable.setLayoutData(gd_cmpoWebAppsTable);
        formToolkit.paintBordersFor(cmpoWebAppsTable);
        
        tableWebApps = new Table(cmpoWebAppsTable, SWT.BORDER | SWT.FULL_SELECTION);
        formToolkit.adapt(tableWebApps);
        formToolkit.paintBordersFor(tableWebApps);
        tableWebApps.setHeaderVisible(true);
        tableWebApps.setLinesVisible(true);
        
        Composite cmpoActionButtons = formToolkit.createComposite(cmpoWebAppOnLinux, SWT.NONE);
        cmpoActionButtons.setLayout(new GridLayout(1, false));
        cmpoActionButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        formToolkit.paintBordersFor(cmpoActionButtons);
        
        Button btnCreate = new Button(cmpoActionButtons, SWT.NONE);
        btnCreate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        formToolkit.adapt(btnCreate, true, true);
        btnCreate.setText("Create");
        
        Button btnRefresh = new Button(cmpoActionButtons, SWT.NONE);
        btnRefresh.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        formToolkit.adapt(btnRefresh, true, true);
        btnRefresh.setText("Refresh");
        
        Button btnDelete = new Button(cmpoActionButtons, SWT.NONE);
        btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        formToolkit.adapt(btnDelete, true, true);
        btnDelete.setText("Delete");
        
        Composite cmpoInformation = formToolkit.createComposite(container, SWT.NONE);
        cmpoInformation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        formToolkit.paintBordersFor(cmpoInformation);
    }
    
    @Override
    protected void finalize() throws Throwable {
		presenter.onDetachView();
        super.finalize();
    }
}
