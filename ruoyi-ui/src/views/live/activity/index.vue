<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="活动编号" prop="activityId">
              <el-input v-model="queryParams.activityId" placeholder="请输入活动编号" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="活动名称" prop="activityName">
              <el-input v-model="queryParams.activityName" placeholder="请输入活动名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
                <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd"
              v-hasPermi="['live:activity:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
              v-hasPermi="['live:activity:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['live:activity:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['live:activity:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="activityList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="活动编号" align="center" prop="activityId" v-if="true" width="100" />
        <el-table-column label="活动名称" align="center" prop="activityName" width="180" :show-overflow-tooltip="true" />
        <el-table-column label="活动说明" align="center" prop="activityRemark" :show-overflow-tooltip="true" />
        <el-table-column label="策略类" align="center" prop="className" :show-overflow-tooltip="true" width="100" />
        <el-table-column label="状态" align="center" prop="status" width="80">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="160">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="更新时间" align="center" prop="updateTime" width="160">
          <template #default="scope">
            <span>{{ parseTime(scope.row.updateTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="88">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['live:activity:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                v-hasPermi="['live:activity:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改活动管理对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="activityFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="活动编号" prop="activityId">
          <el-input v-model="form.activityId" placeholder="请输入活动编号" />
        </el-form-item>
        <el-form-item label="活动名称" prop="activityName">
          <el-input v-model="form.activityName" placeholder="请输入活动名称" />
        </el-form-item>
        <el-form-item label="活动说明" prop="activityRemark">
          <el-input v-model="form.activityRemark" type="textarea" :autosize="true" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="策略类" prop="className">
          <el-input v-model="form.className" placeholder="请输入策略类" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Activity" lang="ts">
  import { listActivity, getActivity, delActivity, addActivity, updateActivity } from '@/api/live/activity';
  import { ActivityVO, ActivityQuery, ActivityForm } from '@/api/live/activity/types';

  const { proxy } = getCurrentInstance() as ComponentInternalInstance;
  const { sys_normal_disable } = toRefs<any>(proxy?.useDict('sys_normal_disable'));

  const activityList = ref<ActivityVO[]>([]);
  const buttonLoading = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const ids = ref<Array<string | number>>([]);
  const single = ref(true);
  const multiple = ref(true);
  const total = ref(0);

  const queryFormRef = ref<ElFormInstance>();
  const activityFormRef = ref<ElFormInstance>();

  const dialog = reactive<DialogOption>({
    visible: false,
    title: ''
  });

  const initFormData : ActivityForm = {
    activityId: undefined,
    activityName: undefined,
    activityRemark: undefined,
    status: '0',
    className: undefined
  }
  const data = reactive<PageData<ActivityForm, ActivityQuery>>({
    form: { ...initFormData },
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      orderByColumn: 'createTime',
      isAsc: 'desc',
      activityId: undefined,
      activityName: undefined,
      activityRemark: undefined,
      status: undefined,
      params: {
      }
    },
    rules: {
      activityId: [
        { required: true, message: "活动编号不能为空", trigger: "blur" }
      ],
      activityName: [
        { required: true, message: "活动名称不能为空", trigger: "blur" }
      ],
      activityRemark: [
        { required: true, message: "活动说明不能为空", trigger: "blur" }
      ],
      status: [
        { required: true, message: "状态不能为空", trigger: "change" }
      ],
    }
  });

  const { queryParams, form, rules } = toRefs(data);

  /** 查询活动管理列表 */
  const getList = async () => {
    loading.value = true;
    const res = await listActivity(queryParams.value);
    activityList.value = res.rows;
    total.value = res.total;
    loading.value = false;
  }

  /** 取消按钮 */
  const cancel = () => {
    reset();
    dialog.visible = false;
  }

  /** 表单重置 */
  const reset = () => {
    form.value = { ...initFormData };
    activityFormRef.value?.resetFields();
  }

  /** 搜索按钮操作 */
  const handleQuery = () => {
    queryParams.value.pageNum = 1;
    getList();
  }

  /** 重置按钮操作 */
  const resetQuery = () => {
    queryFormRef.value?.resetFields();
    handleQuery();
  }

  /** 多选框选中数据 */
  const handleSelectionChange = (selection : ActivityVO[]) => {
    ids.value = selection.map(item => item.activityId);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  const handleAdd = () => {
    reset();
    dialog.visible = true;
    dialog.title = "添加活动管理";
  }

  /** 修改按钮操作 */
  const handleUpdate = async (row ?: ActivityVO) => {
    reset();
    const _activityId = row?.activityId || ids.value[0]
    const res = await getActivity(_activityId);
    Object.assign(form.value, res.data);
    dialog.visible = true;
    dialog.title = "修改活动管理";
  }

  /** 提交按钮 */
  const submitForm = () => {
    activityFormRef.value?.validate(async (valid : boolean) => {
      if (valid) {
        buttonLoading.value = true;
        if (form.value.activityId) {
          await updateActivity(form.value).finally(() => buttonLoading.value = false);
        } else {
          await addActivity(form.value).finally(() => buttonLoading.value = false);
        }
        proxy?.$modal.msgSuccess("操作成功");
        dialog.visible = false;
        await getList();
      }
    });
  }

  /** 删除按钮操作 */
  const handleDelete = async (row ?: ActivityVO) => {
    const _activityIds = row?.activityId || ids.value;
    await proxy?.$modal.confirm('是否确认删除活动管理编号为"' + _activityIds + '"的数据项？').finally(() => loading.value = false);
    await delActivity(_activityIds);
    proxy?.$modal.msgSuccess("删除成功");
    await getList();
  }

  /** 导出按钮操作 */
  const handleExport = () => {
    proxy?.download('live/activity/export', {
      ...queryParams.value
    }, `activity_${new Date().getTime()}.xlsx`)
  }

  onMounted(() => {
    getList();
  });
</script>
